package com.example.rx;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class exceptionTest {

    public Map<String, String> fruits = new HashMap<>();


    @Test
    void test(){

        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("abc", "포도");

        Flux
                .fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .subscribeOn(Schedulers.boundedElastic())
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length() - 1))
                .map(fruits::get)
                .doOnEach(signal -> {
                    if (signal.isOnNext()) System.out.println("Received: " + signal.get());
                    else if (signal.isOnError()) System.out.println("Caught: " + signal.getThrowable().getMessage());
                })
                .map(translated -> "맛있는 " + translated)
                .subscribe();
    }

}
