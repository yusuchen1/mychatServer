package com.wanglongxiang.mychat.exception.UserException;

import com.wanglongxiang.mychat.exception.BaseException;

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
