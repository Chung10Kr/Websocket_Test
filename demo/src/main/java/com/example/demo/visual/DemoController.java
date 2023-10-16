package com.example.demo.visual;

import com.example.demo.visual.visualFlow.FlowType;
import com.example.demo.visual.visualFlow.VisualFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@Slf4j
public class DemoController {


    @GetMapping("/sum")
    public int getSum(){
        int start = 1;
        int end = 1000;

        int result=0;
        for(int i=start;i<end;i++){
            result+=i;
            System.out.println(result);
        }
        return result;

    }
    @GetMapping("/sum/log")
    public int getSumLog(){
        VisualFlow visualFlow = new VisualFlow();
        visualFlow.setRequestId(visualFlow.getClientIP());
        int start = 1;
        int end = 1000;

        int result=0;
        for(int i=start;i<end;i++){
            result+=i;
            visualFlow.collectData(FlowType.DATA,"SUM",result);
        }
        return result;
    }

    @GetMapping("/reduce/log")
    public Mono<Integer> getReduceLog() throws InterruptedException {
        VisualFlow visualFlow = new VisualFlow();
        visualFlow.setRequestId(visualFlow.getClientIP());
        return Flux.range(1,100)
                .doOnEach(signal-> visualFlow.collectData(signal,"Flux Rang"))
                .publishOn(Schedulers.boundedElastic())
                .map(x->x+1)
                .doOnEach(signal-> visualFlow.collectData(signal,"Map"))
                .publishOn(Schedulers.parallel())
                .filter(x->x/2==0)
                .doOnEach(signal-> visualFlow.collectData(signal,"Filter"))
                .publishOn(Schedulers.parallel())
                .reduce(Integer::sum)
                .doOnEach(signal-> visualFlow.collectData(signal,"reduce Rang"));
    }

    @GetMapping("/reduce")
    public Mono<Integer> getReduce() throws InterruptedException {
        return Flux.range(1,100)
                .doOnNext(System.out::println)
                .publishOn(Schedulers.boundedElastic())
                .map(x->x+1)
                .doOnNext(System.out::println)
                .publishOn(Schedulers.parallel())
                .filter(x->x/2==0)
                .doOnNext(System.out::println)
                .publishOn(Schedulers.parallel())
                .reduce(Integer::sum)
                .doOnNext(System.out::println);
    }
}
