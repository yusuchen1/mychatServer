package com.wanglongxiang.mychat.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.wanglongxiang.mychat.exception.BaseException;
import com.wanglongxiang.mychat.properties.AliossProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;

@Component
public class AliossUtil {
    @Autowired
    AliossProperties aliossProperties;
    public String upload(byte[] bytes,String objectName){
        OSS ossClient = new OSSClientBuilder().build(aliossProperties.getEndpoint(),
                aliossProperties.getAccessKeyId(),
                aliossProperties.getAccessKeySecret());
//        上传文件
        try{
            ossClient.putObject(aliossProperties.getBucketName(),objectName,new ByteArrayInputStream(bytes));
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException("文件上传失败!");
        }

//        返回文件名
        return new StringBuilder("http://").append(aliossProperties.getBucketName())
                .append(".")
                .append(aliossProperties.getEndpoint())
                .append("/")
                .append(objectName).toString();
    }
}
