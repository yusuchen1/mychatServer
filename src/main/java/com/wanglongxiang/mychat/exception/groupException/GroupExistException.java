package com.wanglongxiang.mychat.exception.groupException;

import com.wanglongxiang.mychat.exception.BaseException;

public class GroupExistException extends BaseException {
    public GroupExistException(String message) {
        super(message);
    }

    public GroupExistException() {
    }
}
