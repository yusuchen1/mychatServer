package com.wanglongxiang.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("mychat.auth")
public class AuthProperties {
    List<String> excludePaths;
}
