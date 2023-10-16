package com.example.demo.visual.visualFlow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Signal;

import java.lang.management.ManagementFactory;
import java.time.LocalTime;
import java.util.UUID;

public class VisualFlow {

    private MyTextWebSocketHandler webSocketHandler;

    private String requestId;
    private String callClass;
    private String processId;

    public VisualFlow(){
        this.callClass = getCallClass();
        this.processId = getProcessId();
        this.webSocketHandler = new MyTextWebSocketHandler();
    }
    public VisualFlow(String requestId){
        this.requestId = requestId;
        this.callClass = getCallClass();
        this.processId = getProcessId();
        this.webSocketHandler = new MyTextWebSocketHandler();
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    public void collectData(FlowType flowType, String label, Object value){
        if( this.requestId == null ){
            return;
        }

        Flow flow = setFlow(flowType,label,value);
        sendData(flow);
    }

    public void collectData(Signal signal,String label){
        if( this.requestId == null ){
            return;
        }

        Flow flow = new Flow();
        if (signal.isOnNext()){
            flow = setFlow(FlowType.DATA , label , signal.get() );
        }else if (signal.isOnError()){
            flow = setFlow(FlowType.ERROR , label , signal.getThrowable().getMessage() );
        }
        sendData(flow);
    }


    public Flow setFlow(FlowType flowType, String label,Object v){
        Flow flow = new Flow();
        flow.setDataType(flowType);
        flow.setRequestId( this.requestId );

        flow.setLocalTime( LocalTime.now() );
        flow.setCallClass( this.callClass );
        flow.setProcessId( this.processId );
        flow.setThread_name(Thread.currentThread().getName());
        flow.setLabel(label);
        flow.setValue(String.valueOf( v ));
        return flow;
    }

    public void sendData(Flow flow){
        try{
            String jsonString = objectMapper.writeValueAsString(flow);
            webSocketHandler.sendMessage(jsonString);
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
