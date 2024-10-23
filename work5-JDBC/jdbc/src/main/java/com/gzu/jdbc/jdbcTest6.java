package com.gzu.jdbc;

import java.sql.*;

public class jdbcTest6 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "2003729";
        String sql = "SELECT * FROM teacher";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = ps.executeQuery()) {

            // 移动到最后一条记录
            rs.last();
            int totalRecords = rs.getRow();

            // 移动到倒数第二条记录
            if (totalRecords >= 2) {
                rs.absolute(totalRecords - 1);
            } else {
                System.out.println("Less than two records found.");
                return;
            }

            // 提取数据
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String course = rs.getString("course");
            Date birthday = rs.getDate("birthday");

            // 输出数据
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Course: " + course);
            System.out.println("Birthday: " + birthday);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}