package com.wanglongxiang.mychat.webSocket;

import com.alibaba.fastjson2.JSON;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.LChatVO;
import com.wanglongxiang.mychat.properties.JwtProperties;
import com.wanglongxiang.mychat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@ServerEndpoint(value = "/ws/{jwt}")
public class EchoChannel {
    public static final String SecretKey = "xiaosu";

    public static Map<Long, Session> sessions = new HashMap<>();

    /*
     * 连接时调用
     * */
    @OnOpen
    public void onOpen(@PathParam("jwt") String jwt, Session session, EndpointConfig endpointConfig) {
        Long userId = getUserId(jwt);
//        将session存入map中
        log.info("正在为用户：{}建立webSocket连接", userId);
        sessions.put(userId, session);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("前端发来消息:{}", message);
    }

    @OnClose
    public void onClose(@PathParam("jwt") String jwt, CloseReason closeReason) {
        Long userId = getUserId(jwt);
        log.info("{}连接断开...", userId);
    }

    @OnError
    public void onError(@PathParam("jwt") String jwt, Throwable throwable) throws IOException {
        Long userId = getUserId(jwt);
        log.info("连接异常关闭...");
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        sessions.get(userId).close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    public void sendClientByUid(LChatVO chatVOS, Long uid) throws IOException {
        Result<LChatVO> result = new Result<>("聊天消息", chatVOS, Code.SUCESS);
        Session session = sessions.get(uid);
        if (session != null) {
            log.info("正在给uid:{}发送消息", uid);
            session.getBasicRemote().sendText(JSON.toJSONString(result));
        }
    }


    public Long getUserId(String jwt) {
        Claims claims = JwtUtils.parseJWT(SecretKey, jwt);
        return claims.get(UserConstant.UID, Long.class);
    }
}
