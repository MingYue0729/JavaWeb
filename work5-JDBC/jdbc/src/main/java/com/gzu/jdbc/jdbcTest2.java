package com.gzu.jdbc;

import java.sql.*;

//查找 年龄大于22的教师
public class jdbcTest2 {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "2003729";
        // 使用当前日期减去生日，然后与22比较
        String sql = "SELECT * FROM teacher WHERE TIMESTAMPDIFF(YEAR, birthday, CURDATE()) > 22";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            // 执行查询
            try (ResultSet rs = ps.executeQuery()) {
                // 输出查询结果
                while (rs.next()) {
                    System.out.println(rs.getObject("id") + " " + rs.getObject("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}