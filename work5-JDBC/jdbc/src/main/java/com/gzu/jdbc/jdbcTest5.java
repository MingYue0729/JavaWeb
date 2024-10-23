package com.gzu.jdbc;

import java.sql.*;
import java.util.Calendar;
import java.util.Random;

public class jdbcTest5 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
        String user = "root";
        String password = "2003729";
        String sql = "INSERT INTO teacher (id, name, course, birthday) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false); // 关闭自动提交
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int count = 0;
                for (int i = 1; i <= 500; i++) {
                    ps.setInt(1, i); // 设置 id，id是唯一的
                    ps.setString(2, "Teacher" + i); // 设置 name
                    ps.setString(3, "Course" + i); // 设置 course
                    Calendar calendar = Calendar.getInstance();// 设置随机年份的birthday
                    int year = 1950 + new Random().nextInt(51); // 生成1950到2000年之间的随机年份
                    int month = new Random().nextInt(12) + 1; // 生成1到12月之间的随机月份
                    int day = new Random().nextInt(28) + 1; // 简单起见，假设每个月最多28天
                    calendar.set(year, month - 1, day); // 设置随机 birthday
                    ps.setDate(4, new Date(calendar.getTimeInMillis())); // 设置 birthday

                    ps.addBatch();

                    if (++count % 100 == 0) { // 每100条数据执行一次批处理并提交
                        ps.executeBatch();
                        conn.commit();
                    }
                }
                if (count % 100 != 0) { // 处理剩余的数据
                    ps.executeBatch();
                    conn.commit();
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