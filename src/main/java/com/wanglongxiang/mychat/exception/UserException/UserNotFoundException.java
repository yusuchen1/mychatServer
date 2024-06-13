package com.wanglongxiang.mychat.exception.UserException;

import com.wanglongxiang.mychat.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
    }
}
