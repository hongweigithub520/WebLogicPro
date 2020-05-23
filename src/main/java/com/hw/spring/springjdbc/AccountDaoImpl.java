package com.hw.spring.springjdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class AccountDaoImpl implements AccountDao {
    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void out(String outUser, int money) {
        this.jdbcTemplate.update("update account set money =money-?"
                + "where username =?", money, outUser);
    }


    public void in(String inUser, int money) {
        this.jdbcTemplate.update("update account set money =money+?"
                + "where username =?", money, inUser);
    }
}
