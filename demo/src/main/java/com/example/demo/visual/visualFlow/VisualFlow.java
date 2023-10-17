package com.example.demo.visual.visualFlow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.java_websocket.client.WebSocketClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Signal;

import java.lang.management.ManagementFactory;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class VisualFlow {

    private final WebSocketClient webSocketClient;

    public VisualFlow(WebSocketClient webSocketClient){
        this.webSocketClient = webSocketClient;
    }

    public Map<String,Object> getStatus(String requestId) {
        Map<String,Object> res = new HashMap<>();
        res.put("requestId" , requestId == null ? getClientIP() : requestId);
        res.put("callClass" , getCallClass());
        res.put("processId" , getProcessId());
        return res;
    }

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    public void collectData(Map<String,Object> option, FlowType flowType, String label, Object value){

        Flow flow = setFlow(option,flowType,label,value);
        sendData(flow);
    }

    public void collectData(Map<String,Object> option, Signal signal,String label){

        Flow flow = new Flow();
        if (signal.isOnNext()){
            flow = setFlow(option,FlowType.DATA , label , signal.get() );
        }else if (signal.isOnError()){
            flow = setFlow(option,FlowType.ERROR , label , signal.getThrowable().getMessage() );
        }
        sendData(flow);
    }


    public Flow setFlow(Map<String,Object> option, FlowType flowType, String label,Object v){
        Flow flow = new Flow();
        flow.setDataType(flowType);
        flow.setRequestId(option.get("requestId").toString());

        flow.setLocalTime( LocalTime.now() );
        flow.setCallClass(option.get("callClass").toString());
        flow.setProcessId(option.get("processId").toString());
        flow.setThread_name(Thread.currentThread().getName());
        flow.setLabel(label);
        flow.setValue(String.valueOf( v ));
        return flow;
    }

    public void sendData(Flow flow){
        if( flow.getRequestId() == null ) return;

        try{
            String jsonString = objectMapper.writeValueAsString(flow);
//            webSocketClient.send(jsonString);

            CompletableFuture.runAsync(() -> {
                webSocketClient.send(jsonString);
            });

        }catch (JsonProcessingException e){

        }

    }

    public String getClientIP(){
        String result = null;
        try{
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            result = req.getHeader("X-FORWARDED-FOR");
            if ( result == null){
                result = req.getRemoteAddr();
            };
        }catch(Exception e){
            result = String.valueOf( UUID.randomUUID() );
        };
        return result;
    }
    private String getCallClass(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 3) {
            StackTraceElement caller = stackTraceElements[3];
            return caller.getClassName()+"."+caller.getMethodName();
        }
        return null;
    }
    private String getProcessId(){
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf('@');
        if (index > 0) {
            String pid = jvmName.substring(0, index);
            return pid;
        }
        return null;
    }

}
