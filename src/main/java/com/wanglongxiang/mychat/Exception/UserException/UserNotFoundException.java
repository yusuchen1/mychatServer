package com.wanglongxiang.mychat.Exception.UserException;

import com.wanglongxiang.mychat.Exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
    }
}
