package com.wanglongxiang.mychat.exception.UserException;

import com.wanglongxiang.mychat.exception.BaseException;

public class UserExistException extends BaseException {
    public UserExistException(String message) {
        super(message);
    }

    public UserExistException() {
    }
}
