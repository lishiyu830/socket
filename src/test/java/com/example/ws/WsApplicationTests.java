package com.example.ws;

import com.example.ws.entity.PushMessage;
import com.example.ws.service.SocketIOService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WsApplicationTests {

    @Resource(name = "socketIOService")
    SocketIOService socketIOService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSocket() {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setLoginUserId("88");
        pushMessage.setContent("I am from server");
        socketIOService.pushMessageToUser(pushMessage);
    }
}
