package com.example.rx;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;


public class proxyTest {

    @Test
    public void main() {
        Flux flux = Flux.just(1,2,3,4,5,6,7,8,9,10,11,12);
        // 프록시 생성
        Flux<Integer> proxy = (Flux<Integer>) Proxy.newProxyInstance(
                flux.getClass().getClassLoader(),
                new Class<?>[]{Flux.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object result = method.invoke(flux, args);
                        if (result instanceof Flux<?>) {  // 만약 결과가 Flux라면,
                            return ((Flux<?>) result).doOnNext(value -> System.out.println("Value: " + value));  // 로그 출력 코드 추가
                        } else {
                            return result;
                        }
                    }
                });

        // 프록시를 통해 메소드 호출
        proxy.map(x -> x+1 ).subscribe();
    }
}
