package com.example.ws.webSocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * Created by lsy on 2018/10/23
 */
@Component
@ServerEndpoint(value = "/monitorTrade/{token}")
@Slf4j
public class MonitorTrade {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<MonitorTrade> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    private static RedisTemplate<String, Object> redisTemplate;

    private String webSocketUuid;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        MonitorTrade.redisTemplate = redisTemplate;
    }

    private String riskWebSocketPrefix = "risk:webSocket:";

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(riskWebSocketPrefix, uuid, session.getId());
        this.webSocketUuid = uuid;
        Map<String, String> map = new HashMap<>();
        map.put("webSocketId", uuid);
        sendMessage(JSON.toJSONString(map), session);
        log.info("webSocket monitorTrade a user add, uuid={}, sessionId={}", uuid, session.getId());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        redisTemplate.opsForHash().delete(riskWebSocketPrefix, this.webSocketUuid);
//        Collection<Session> values = sessionMap.values();
//        values.remove(this.session);
        subOnlineCount();
        log.info("webSocket monitorTrade a user close, session: {}", this.session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        for (MonitorTrade item : webSocketSet) {
            if (item.session.getId().equals(session.getId())) {
                item.sendMessage(message, session);
            }
        }
    }

    public void sendMessage(String jsonStr, String uuid) {
        try {
            for (MonitorTrade item : webSocketSet) {
                TimeUnit.MICROSECONDS.sleep(1000);
                if (StringUtils.isNotEmpty(uuid)) {
                    String redisSessionId = redisTemplate.opsForHash().get(riskWebSocketPrefix, uuid) + "";
                    if (item.session.getId().equals(redisSessionId)) {
                        item.sendMessage(jsonStr, item.session);
                        log.info("webSocket sendMessage success, sessionId:{}, time:{}, uuid:{}", redisSessionId, System.currentTimeMillis(), uuid);
                    }
                } else {
                    item.sendMessage(jsonStr, item.session);
                    log.info("webSocket sendMessage success, uuid is null, sessionId:{}, time:{}", item.session.getId(), System.currentTimeMillis());
                }
            }
        } catch (Exception e) {
            log.warn("webSocket monitorTrade send message exception, msg:{}", e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("webSocket monitorTrade error, json trace:{}", JSON.toJSONString(error.getStackTrace()));
        log.error("webSocket monitorTrade error, session:{}, message: {}", session.getId(), error.getMessage());
    }

    private void sendMessage(String message, Session session){
        try {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void subOnlineCount() {
        MonitorTrade.onlineCount--;
    }

    private static synchronized void addOnlineCount() {
        MonitorTrade.onlineCount++;
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }
}
