package com.wanglongxiang.user.exception.UserException;

import com.wanglongxiang.mychat.exception.BaseException;

public class PasswordErrorException extends BaseException {
    public PasswordErrorException(String message) {
        super(message);
    }

    public PasswordErrorException() {
    }
}
