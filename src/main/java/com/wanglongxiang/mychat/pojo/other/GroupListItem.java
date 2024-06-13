package com.wanglongxiang.mychat.pojo.other;

import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListItem {
    private Long id;
    private String avatar;
    private String name;
    private List<ChatVO> chats;
}
