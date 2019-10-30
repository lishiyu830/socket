package com.example.ws.entity;

import lombok.Data;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
@Data
public class PushMessage {

    private String loginUserId;

    private String loginUsername;

    private String content;
}
