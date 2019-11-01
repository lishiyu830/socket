package com.example.ws.dao;

import com.example.ws.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by lsy on 2019-10-31
 * lishiyu@chinamobile.com
 */
@Mapper
public interface UserMapper {

    @Select("select * from ws_user where username = #{username}")
    @Results(id = "userResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
    })
    User selectByUsername(String username);

    @Select("select * from ws_user where id = #{userId}")
    @ResultMap(value = "userResultMap")
    User selectByUserId(Integer userId);
}
