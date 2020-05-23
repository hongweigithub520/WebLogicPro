package com.hw.webmagic.dao;

import com.hw.webmagic.entity.SuccDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SuccDetailDao {
    //insert数据
    public void insert(Connection conn, SuccDetail succDetail) throws SQLException {
        //conn.setAutoCommit(false);
        String sql = "insert into success_table(cover,title,need,success,type,area" +
                ",praise) values (?,?,?,?,?,?,?)";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, succDetail.getCover());
        pstm.setString(2, succDetail.getTitle());
        pstm.setString(3, succDetail.getNeed());
        pstm.setString(4, succDetail.getSuccess());
        pstm.setString(5, succDetail.getType());
        pstm.setString(6, succDetail.getArea());
        pstm.setString(7, succDetail.getPraise());
        pstm.executeUpdate();
        //conn.commit();
    }
}
