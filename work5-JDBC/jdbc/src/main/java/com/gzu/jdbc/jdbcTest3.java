package com.gzu.jdbc;

import java.sql.*;

//更新
public class jdbcTest3 {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "2003729";
        // 修改数据 id =1, name=张三（改为王五）
        String sql = "UPDATE teacher SET name = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                // 设置参数
                ps.setString(1, "王五"); // name 是字符串类型
                ps.setInt(2, 1); // id 是 INT 类型
                // 执行更新
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    System.out.println("Update successful. Rows affected: " + affectedRows);
                } else {
                    conn.rollback();
                    System.out.println("No rows updated.");
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}