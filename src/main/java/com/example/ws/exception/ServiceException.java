package com.example.ws.exception;

/**
 * Created by lsy on 2019-10-31
 * lishiyu@chinamobile.com
 */
public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String message;
    private String code;

    public ServiceException(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
