package com.example.ws.service;

import com.example.ws.entity.PushMessage;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
public interface SocketIOService {

    public static final String PUSH_EVENT = "push_event";

    void start() throws Exception;

    void stop();

    void pushMessageToUser(PushMessage pushMessage);
}