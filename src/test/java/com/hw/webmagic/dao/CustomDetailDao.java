package com.hw.webmagic.dao;

import com.hw.webmagic.entity.CustomDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomDetailDao {
        //往数据库中添加数据
        public int add(Connection con, CustomDetail customdetail) throws SQLException {
            String sql = "insert into investor_table(avatar,name,position,company,address,type,phase" +
                    ",field,userDesc,companyDesc) values (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            System.out.print(customdetail.getAvatar()+"22222222");
            pstm.setString(1, customdetail.getAvatar());
            pstm.setString(2, customdetail.getName());
            pstm.setString(3, customdetail.getPosition());
            pstm.setString(4, customdetail.getCompany());
            pstm.setString(5, customdetail.getAddress());
            pstm.setString(6, customdetail.getType());
            pstm.setString(7, customdetail.getPhase());
            pstm.setString(8, customdetail.getField());
            pstm.setString(9, customdetail.getUserDesc());
            pstm.setString(10, customdetail.getCompanyDesc());
            return pstm.executeUpdate();
        }

}
