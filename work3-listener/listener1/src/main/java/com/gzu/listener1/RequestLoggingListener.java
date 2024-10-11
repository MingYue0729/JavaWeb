package com.gzu.listener1;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

@WebListener
public class RequestLoggingListener implements ServletRequestListener {
    private static final Logger LOGGER = Logger.getLogger(RequestLoggingListener.class.getName());

    static {
        try {
            // 设置日志文件路径
            File logDir = new File("D:\\JavaWeb\\logs");
            if (!logDir.exists()) {
                logDir.mkdirs(); // 如果目录不存在，则创建目录
            }
            File logFile = new File(logDir, "request-logs.log");
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
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime == null) {
            LOGGER.warning("Start time not recorded, skipping logging for this request.");
            return;
        }
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedEndTime = dateFormat.format(new Date(endTime));

        LOGGER.info(String.format("请求结束时间: %s,\n 客户端IP地址: %s,\n 请求方法: %s,\n 请求URI: %s,\n 查询字符串: %s,\n User-Agent: %s,\n 请求处理时间: %d ms\n",
                formattedEndTime, // 请求结束时间
                request.getRemoteAddr(), // 客户端IP地址
                request.getMethod(), // 请求方法
                request.getRequestURI(), // 请求URI
                request.getQueryString(), // 查询字符串
                request.getHeader("User-Agent"), // User-Agent
                processingTime // 请求处理时间
        ));
    }
}