package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
public class GroupTest {
    @Autowired
    GroupService groupService;
    @Test
    public void joinGroupTest() throws IOException {
        groupService.joinGroup(3L,6L);
    }
}
