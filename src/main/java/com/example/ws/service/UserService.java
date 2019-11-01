package com.example.ws.service;

import com.example.ws.dao.UserMapper;
import com.example.ws.entity.User;
import com.example.ws.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsy on 2019-10-31
 * lishiyu@chinamobile.com
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private static final Map<String, String> userMap = new HashMap<>();
    static {
        userMap.put("lsy", "lsy");
        userMap.put("jay", "jay");
        userMap.put("xxx", "xxx");
    }

    public User getUserInfo(Integer userId) {
        return userMapper.selectByUserId(userId);
    }

    public User login(String username, String password) {
//        if (userMap.containsKey(username)) {
//            String pwd = userMap.get(username);
//            if (pwd.equals(password)) {
//                return "0";
//            } else {
//                return "密码错误";
//            }
//        } else {
//            return "用户名不存在";
//        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new ServiceException("5002");
        }
        //@TODO password not check

        return user;
    }
}
