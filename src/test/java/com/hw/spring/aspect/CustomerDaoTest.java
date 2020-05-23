package com.hw.spring.aspect;

import com.hw.spring.springjdbc.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CustomerDaoTest {
    @Test
    public void test0(){
        CustomerDao cd=MyBeanFactory.getBean();

        cd.add();
        cd.delete();
        cd.find();
    }

    @Test
    public void test1(){
        CustomerDao cd=MyBeanFactory.getBean1();

        cd.add();
        cd.delete();
        cd.find();
    }
    //通过xml配置文件来进行配置
    @Test
    public void test2() {
        String xmlPath = "applicationaspetContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                xmlPath);
        CustomerDao customerDao = (CustomerDao) applicationContext
                .getBean("customerDaoProxy");
        customerDao.add();
        customerDao.update();
        customerDao.delete();
        customerDao.find();
    }
    //基于XML的声明式
    @Test
    public void test4() {
        String xmlPath = "applicationaspet1Context.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                xmlPath);
        // 从spring容器获取实例
        CustomerDao customerDao = (CustomerDao) applicationContext
                .getBean("customerDao");
        // 执行方法
        customerDao.add();
    }
    @Test
    public void test5() {
        String xmlPath = "applicationaspet2Context.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
        // 从spring容器获取实例
        CustomerDao customerDao = (CustomerDao) applicationContext
                .getBean("customerDao");
        // 执行方法
        customerDao.add();
    }

    @Test
    public void test6() {
        // 获得Spring容器，并操作
        String xmlPath = "applicationContextjdbc.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                xmlPath);
        AccountService accountService = (AccountService) applicationContext
                .getBean("accountService");
        accountService.transfer("zhangsan", "lisi", 100);
    }
}