package com.gzu.jdbc;

import java.sql.*;
import java.time.LocalDate;

//创建
public class jdbcTest1 {
    private static final String url = "jdbc:mysql://localhost:3306/jdbc_teacher?serverTimezone=GMT&characterEncoding=UTF-8";
    private static final String user = "root";
    private static final String passward = "2003729";

    public static void createTeacher(int id, String name, String course, String birthday) {
        String sql = "INSERT INTO teacher (id, name, course, birthday) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, passward);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, course);
            ps.setDate(4, Date.valueOf(birthday));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 创建两个新的教师记录
        int id1 = 1;
        String name = "张三";
        String course = "语文";
        LocalDate birthday = LocalDate.of(1999, 10, 23); // 假设生日是1980年5月20日
        createTeacher(id1, name, course, birthday.toString());

        int id2 = 2;
        String name2 = "李四";
        String course2 = "数学";
        LocalDate birthday2 = LocalDate.of(2000, 10, 30); // 假设生日是1980年5月20日
        createTeacher(id2, name2, course2, birthday2.toString());
    }
}


