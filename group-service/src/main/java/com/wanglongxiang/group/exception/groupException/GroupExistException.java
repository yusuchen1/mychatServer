package com.wanglongxiang.group.exception.groupException;

import com.wanglongxiang.mychat.exception.BaseException;

public class GroupExistException extends BaseException {
    public GroupExistException(String message) {
        super(message);
    }

    public GroupExistException() {
    }
}
