package com.example.demo.visual.ctr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Slf4j
@RestController
@RequestMapping("/slf4j")
public class LoggingController {

    @GetMapping("/sum")
    public int getSum(){
        int start = 1;
        int end = 1000;

        int result=0;
        for(int i=start;i<end;i++){
            result+=i;
            log.info(String.valueOf(result));
        }
        return result;
    }
    @GetMapping("/reactor")
    public Mono<Integer> getReduceLog() throws InterruptedException {
        return Flux.range(1,100)
                .doOnNext(x->log.info(String.valueOf(x)))
                .publishOn(Schedulers.boundedElastic())
                .map(x->x+1)
                .doOnNext(x->log.info(String.valueOf(x)))
                .publishOn(Schedulers.parallel())
                .filter(x->x/2==0)
                .doOnNext(x->log.info(String.valueOf(x)))
                .publishOn(Schedulers.parallel())
                .reduce(Integer::sum)
                .doOnNext(x->log.info(String.valueOf(x)));
    }
}
