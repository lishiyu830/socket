package com.example.ws.config;

import com.example.ws.entity.PushMessage;
import com.example.ws.service.SocketIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
@Configuration
@WebListener
public class SocketIOListener implements ServletContextListener {

    @Autowired
    SocketIOService socketIOService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            socketIOService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        socketIOService.stop();
    }
}
