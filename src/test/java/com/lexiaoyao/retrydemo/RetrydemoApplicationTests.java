package com.lexiaoyao.retrydemo;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.lexiaoyao.retrydemo.component.AppRetryerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Slf4j
public class RetrydemoApplicationTests {

    int times = 1;

    @Autowired
    private AppRetryerBuilder retryerBuilder;

    @Test
    public void contextLoads() throws ExecutionException, RetryException {

        Retryer<Boolean> retryer = retryerBuilder.build();

        //定义请求实现
        Callable<Boolean> callable = () -> {
            log.info("call times={}", times);
            times++;
            if (times == 2) {
                throw new RuntimeException();
            } else if (times == 3) {
                throw new Exception();
            } else if (times == 4) {
                return false;
            } else return times != 5;
        };
        //利用重试器调用请求
        try {
            Boolean call = retryer.call(callable);//也可以不用try，call方法抛出的是运行时异常ss
            System.out.println(call);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("finish");

//        builder.build().call(task);
    }

}
