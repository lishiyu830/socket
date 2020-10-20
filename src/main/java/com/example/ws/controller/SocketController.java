package com.example.ws.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.ws.entity.PushMessage;
import com.example.ws.entity.User;
import com.example.ws.service.SocketIOService;
import com.example.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
@RestController
@RequestMapping(value = "/socket")
@CrossOrigin
public class SocketController {

    @Autowired
    SocketIOService socketIOService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public void test(String message) {

        PushMessage pushMessage = new PushMessage();
        pushMessage.setLoginUserId("88");
        pushMessage.setContent(message);
        socketIOService.pushToAll("hello");
    }

    @RequestMapping(value = "/sendMsg", method = {RequestMethod.POST})
    public void sendMessage(Integer userId, String message) {
        User user = userService.getUserInfo(userId);

        PushMessage pushMessage = new PushMessage();
        pushMessage.setLoginUserId(userId.toString());
        pushMessage.setLoginUsername(user.getUsername());
        pushMessage.setContent(message);
        socketIOService.pushToAllExceptOne(pushMessage, userId);
    }

    @RequestMapping(value = "/change-menu", method = {RequestMethod.GET})
    public void changeMenu(String menu, String grid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menu", menu);
        jsonObject.put("grid", grid);
        socketIOService.pushToAll(jsonObject.toJSONString());
    }
}
