package com.hw.spring.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest {
    @Test
    public void demo01() {
        //传统的方式创建对象
        UserServiceImpl userServiceimpl = new UserServiceImpl();
        userServiceimpl.setName("hw--传统");
        userServiceimpl.addUser();
    }
    @Test
    public void demo02() {
        //从spring容器获得
        //1 获得容器
        String xmlPath = "applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        //2获得内容 --不需要自己new，都是从spring容器获得
        UserService userService = (UserService) applicationContext.getBean("userServiceId");
        userService.addUser();
    }
    @Test
    public void demo03() {
        //从spring容器获得
        //1 获得容器
        String xmlPath = "applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        // 设值方式输出结果
        System.out.println(applicationContext.getBean("person1"));
        // 构造方式输出结果
        System.out.println(applicationContext.getBean("person2"));
    }
}