package com.wanglongxiang.chat.webSocket;

import com.alibaba.fastjson2.JSON;
import com.wanglongxiang.api.client.CommonClient;
import com.wanglongxiang.api.client.CronyClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.vo.LChatVO;
import com.wanglongxiang.api.vo.OnlineInfoVO;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.util.JwtUtils;
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

@Slf4j
@Component
@ServerEndpoint(value = "/ws/{jwt}")
public class EchoChannel {
    public static final String SecretKey = "xiaosu";

    public static Map<Long, Session> sessions = new HashMap<>();

    private static CommonClient commonClient;

    @Autowired
    public void setCommonClient(CommonClient commonClient) {
        EchoChannel.commonClient = commonClient;
    }

    private static CronyClient cronyClient;

    @Autowired
    public void setCronyClient(CronyClient cronyClient){
        EchoChannel.cronyClient = cronyClient;
    }



    /*
     * 连接时调用
     * */
    @OnOpen
    public void onOpen(@PathParam("jwt") String jwt, Session session, EndpointConfig endpointConfig) {
        Long userId = getUserId(jwt);
//        将session存入map中
        log.info("正在为用户：{}建立webSocket连接", userId);
        sessions.put(userId, session);
        commonClient.onLine(userId);
        notifyOnline(userId,true);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("前端发来消息:{}", message);
    }

    @OnClose
    public void onClose(@PathParam("jwt") String jwt, CloseReason closeReason) {
        Long userId = getUserId(jwt);
        log.info("{}连接断开...", userId);
        commonClient.offLine(userId);
        notifyOnline(userId,false);
    }

    @OnError
    public void onError(@PathParam("jwt") String jwt, Throwable throwable) throws IOException {
        Long userId = getUserId(jwt);
        log.info("连接异常关闭...");
        commonClient.offLine(userId);
        notifyOnline(userId,false);
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

    public void notifyOnline(Long uid,Boolean online){
        List<Long> cronyIds = cronyClient.getCronyIds(uid);
        for (Long cronyId : cronyIds) {
            Session session = sessions.get(cronyId);
            if(session != null){
                if(online){
                    log.info("正在给uid:{}推送好友uid:{}上线消息",cronyId,uid);
                }else {
                    log.info("正在给uid:{}推送好友uid:{}下线消息",cronyId,uid);
                }
                try {
                    OnlineInfoVO oiv = new OnlineInfoVO(uid, online);
                    Result<OnlineInfoVO> result = new Result<>("online信息", oiv, Code.SUCESS);
                    session.getBasicRemote().sendText(JSON.toJSONString(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Long getUserId(String jwt) {
        Claims claims = JwtUtils.parseJWT(SecretKey, jwt);
        return claims.get(UserConstant.UID, Long.class);
    }
}
