package com.wanglongxiang.api.vo;

import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.GroupListItem;
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
