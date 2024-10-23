package com.gzu.jdbc;

import java.sql.*;

public class jdbcTest4 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "2003729";
        // 删除名字为李四或王五的老师
        String sql = "DELETE FROM teacher WHERE name IN (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // 设置参数
                ps.setString(1, "李四");
                ps.setString(2, "王五");
                // 执行删除
                int affectedRows = ps.executeUpdate();
                System.out.println(affectedRows + " rows affected.");
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}