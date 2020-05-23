package com.hw.spring.dao;

public class UserDao implements User {
    @Override
    public void add() {
        System.out.println("spring的setting注入");
    }
}
