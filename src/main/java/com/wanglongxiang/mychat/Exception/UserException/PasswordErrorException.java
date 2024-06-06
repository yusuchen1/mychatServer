package com.wanglongxiang.mychat.Exception.UserException;

import com.wanglongxiang.mychat.Exception.BaseException;

public class PasswordErrorException extends BaseException {
    public PasswordErrorException(String message) {
        super(message);
    }

    public PasswordErrorException() {
    }
}
