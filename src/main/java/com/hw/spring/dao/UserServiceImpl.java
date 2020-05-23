package com.hw.spring.dao;

public class UserServiceImpl implements UserService {
    private User user;
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public void addUser() {
        user.add();
        System.out.println("my name is "+name);
    }


}