package com.wanglongxiang.chat.conf;

import com.wanglongxiang.chat.webSocket.EchoChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


@Configuration
public class WebSocketConfiguration {
    @Bean
    @Profile("!test")
    public ServerEndpointExporter serverEndpointExporter(){
        ServerEndpointExporter serverEndpointExporter = new ServerEndpointExporter();
        serverEndpointExporter.setAnnotatedEndpointClasses(EchoChannel.class);
        return serverEndpointExporter;
    }
}

