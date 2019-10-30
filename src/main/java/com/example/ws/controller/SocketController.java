package com.example.ws.controller;

import com.example.ws.entity.PushMessage;
import com.example.ws.service.SocketIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
@RestController
@RequestMapping(value = "/socket")
public class SocketController {

    @Autowired
    SocketIOService socketIOService;

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public void sendMessage(String message) {

        PushMessage pushMessage = new PushMessage();
        pushMessage.setLoginUserId("88");
        pushMessage.setContent(message);
        socketIOService.pushMessageToUser(pushMessage);
    }
}
