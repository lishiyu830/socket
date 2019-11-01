package com.example.ws.controller;

import com.example.ws.common.ResponseMessage;
import com.example.ws.entity.User;
import com.example.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lsy on 2019-10-31
 * lishiyu@chinamobile.com
 */
@RestController
@RequestMapping(value = "/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = {RequestMethod.POST})
    public ResponseMessage login(String username, String password) {
        ResponseMessage message = new ResponseMessage();
        User user = userService.login(username, password);
        message.setData(user);
        return message;
    }
}
