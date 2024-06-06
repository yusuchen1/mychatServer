package com.wanglongxiang.mychat.Exception.UserException;

import com.wanglongxiang.mychat.Exception.BaseException;

public class UserExistException extends BaseException {
    public UserExistException(String message) {
        super(message);
    }

    public UserExistException() {
    }
}
