package com.example.ws.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lsy on 2019-07-25
 * lishiyu@chinamobile.com
 */
@Data
public class ResponseMessage implements Serializable {
    private static final long serialVersionUID = 46192584250174086L;

    private String code;
    private String message;
    private Object data;

    public ResponseMessage() {
        this.code = ResponseCode.SUCCESS.getCode();
        this.message = ResponseCode.SUCCESS.getMessage();
    }

    public ResponseMessage(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    private static ValueFilter filter = (obj, s, v) -> {
        if (v == null)
            return "";
        return v;
    };

    public String toString() {
        return JSON.toJSONString(this, filter, SerializerFeature.WriteMapNullValue);
    }
}
