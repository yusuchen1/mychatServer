package com.wanglongxiang.mychat.Exception.UserException;

import com.wanglongxiang.mychat.Exception.BaseException;

public class UserUnLoginException extends BaseException {
    public UserUnLoginException(String message) {
        super(message);
    }

    public UserUnLoginException(String message, Integer code) {
        super(message, code);
    }

    public UserUnLoginException() {
    }
}
