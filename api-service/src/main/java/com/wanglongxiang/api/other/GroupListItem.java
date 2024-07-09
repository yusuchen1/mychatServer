package com.wanglongxiang.api.other;

import com.wanglongxiang.api.vo.ChatVO;
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
