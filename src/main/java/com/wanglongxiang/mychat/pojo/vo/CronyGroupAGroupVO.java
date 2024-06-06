package com.wanglongxiang.mychat.pojo.vo;

import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronyGroupAGroupVO {
    private List<GroupListItem> groups;
    private List<CronyGroupList> cronyGroups;
}
