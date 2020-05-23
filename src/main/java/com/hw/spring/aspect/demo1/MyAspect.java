package com.hw.spring.aspect.demo1;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyAspect implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("方法执行之前");
        // 执行目标方法
        Object obj = invocation.proceed();
        System.out.println("方法执行之后");
        return obj;
    }
}