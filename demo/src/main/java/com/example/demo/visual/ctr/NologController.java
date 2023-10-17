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
public class NologController {

    private final VisualFlow visualFlow;

    public NologController(VisualFlow visualFlow) {
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
        }
        return result;
    }

    @GetMapping("/reactor")
    public Mono<Integer> getReduceLog() throws InterruptedException {
        return Flux.range(1,100)
                .publishOn(Schedulers.boundedElastic())
                .map(x->x+1)
                .publishOn(Schedulers.parallel())
                .filter(x->x/2==0)
                .publishOn(Schedulers.parallel())
                .reduce(Integer::sum);
    }
}
