package com.gzu.listener1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.io.IOException;
import java.util.logging.*;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TestServlet.class.getName());

    static {
        try {
            // 设置日志文件路径
            File logDir = new File("D:\\JavaWeb\\logs");
            if (!logDir.exists()) {
                logDir.mkdirs(); // 如果目录不存在，则创建目录
            }
            File logFile = new File(logDir, "test-servlet-logs.log");
            FileHandler fileHandler = new FileHandler(logFile.getAbsolutePath(), true);
            fileHandler.setLevel(Level.INFO);

            // 自定义日志格式器
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    return String.format("%1$tF %1$tT %2$s %3$s%n", new Date(), record.getLevel(), formatMessage(record));
                }
            });

            // 清除之前可能存在的所有Handlers
            LOGGER.setUseParentHandlers(false);
            while (LOGGER.getHandlers().length > 0) {
                LOGGER.removeHandler(LOGGER.getHandlers()[0]);
            }

            // 将FileHandler添加到Logger
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize FileHandler", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LOGGER.info("Starting doGet");
            Thread.sleep(1000); // 模拟延迟
            LOGGER.info("doGet completed");
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Interrupted during doGet", e);
            Thread.currentThread().interrupt();
        }
        resp.getWriter().write("<h1>Welcome to my listener!</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}