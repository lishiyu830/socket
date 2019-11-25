package com.example.ws.entity;

import lombok.Data;

/**
 * Created by lsy on 2019-11-25
 */
@Data
public class MessageInfo {

    private Integer userId;
    private Integer toId;
    private String username;
    private String toName;
    private String message;
}
