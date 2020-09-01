package com.lexiaoyao.retrydemo.component;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AppRetryerBuilder {

    public Retryer<Boolean> build() {
        return RetryerBuilder.<Boolean>newBuilder()
                //如果result为false则重试
                .retryIfResult(i -> i.equals(false))
                //如果执行中有异常则重试
                .retryIfException()
                //每次重试之间间隔三秒
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                //最大重试次数，当达到这个次数则抛出ExecutionException异常
                .withStopStrategy(StopStrategies.stopAfterAttempt(10))
                //RetryListener可以监控多次重试过程，并可以使用attempt做一些额外的事情
                .withRetryListener(new RetryLogListener())
                .build();

    }

}
