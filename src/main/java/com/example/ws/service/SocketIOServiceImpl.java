package com.example.ws.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.ws.entity.MessageInfo;
import com.example.ws.entity.PushMessage;
import com.example.ws.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lsy on 2019-10-23
 * lishiyu@chinamobile.com
 */
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {

    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private UserService userService;

//    @PostConstruct
//    private void autoStartup() throws Exception {
//
//        start();
//    }

//    @PreDestroy
//    private void autoStop() throws Exception {
//        stop();
//    }

    @Override
    public void start() throws Exception {

        //监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String wsid = getParamsByClient(client);
            if (wsid != null) {
                clientMap.put(wsid, client);
            }
            System.out.println("websocket connect ...");
        });

        //监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String wsid = getParamsByClient(client);
            if (wsid != null) {
                clientMap.remove(wsid);
                client.disconnect();
            }
            System.out.println("websocket disconnect ...");
        });

//        自定义事件
        socketIOServer.addEventListener("sendMessage", MessageInfo.class, ((client, messageInfo, ackRequest) -> {
            User user = userService.getUserInfo(messageInfo.getUserId());
            messageInfo.setUsername(user.getNickName());
            clientMap.forEach((s, clientAll) -> {
                clientAll.sendEvent("send_message", messageInfo);
            });
        }));

        socketIOServer.start();
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessageToUser(PushMessage pushMessage) {
        String loginUserNum = pushMessage.getLoginUserId();
        if (StringUtils.isNotBlank(loginUserNum)) {
            SocketIOClient client = clientMap.get(loginUserNum);
            if (client != null) {
                client.sendEvent(PUSH_EVENT, pushMessage);
            }
        }
    }

    @Override
    public void pushToAll(String message) {
        clientMap.forEach((s, client) -> {
            client.sendEvent(PUSH_EVENT, message);
        });
    }

    @Override
    public void pushToAllExceptOne(PushMessage pushMessage, Integer userId) {
        clientMap.forEach((s, client) -> {
            if (!s.equals(userId.toString())) {
                client.sendEvent(PUSH_EVENT, pushMessage);
            }
        });
    }

    private String getParamsByClient(SocketIOClient client) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("wsid");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
