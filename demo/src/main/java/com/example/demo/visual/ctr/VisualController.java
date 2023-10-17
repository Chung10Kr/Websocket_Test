package com.example.demo.visual.ctr;

import com.example.demo.visual.visualFlow.FlowType;
import com.example.demo.visual.visualFlow.VisualFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@RestController
@RequestMapping("/websocket")
public class VisualController {

    private final VisualFlow visualFlow;

    public VisualController(VisualFlow visualFlow) {
        this.visualFlow = visualFlow;
    }

    @GetMapping("/sum")
    public int getSumLog(){
        Map<String,Object> map = visualFlow.getStatus(visualFlow.getClientIP());
        int start = 1;
        int end = 1000;

        int result=0;
        for(int i=start;i<end;i++){
            result+=i;
            visualFlow.collectData(map,FlowType.DATA,"SUM",result);
        }
        return result;
    }

    @GetMapping("/reactor")
    public Mono<Integer> getReduceLog() throws InterruptedException {
        Map<String,Object> map = visualFlow.getStatus(visualFlow.getClientIP());
        return Flux.range(1,100)
                .doOnEach(signal-> visualFlow.collectData(map,signal,"Flux Rang"))
                .publishOn(Schedulers.boundedElastic())
                .map(x->x+1)
                .doOnEach(signal-> visualFlow.collectData(map,signal,"Map"))
                .publishOn(Schedulers.parallel())
                .filter(x->x/2==0)
                .doOnEach(signal-> visualFlow.collectData(map,signal,"Filter"))
                .publishOn(Schedulers.parallel())
                .reduce(Integer::sum)
                .doOnEach(signal-> visualFlow.collectData(map,signal,"reduce Rang"));
    }
}
