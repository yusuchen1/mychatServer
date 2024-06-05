package com.wanglongxiang.mychat.Execption.UserExecption;

import com.wanglongxiang.mychat.Execption.BaseExecption;

public class UserUnLoginExecption extends BaseExecption {
    public UserUnLoginExecption(String message) {
        super(message);
    }

    public UserUnLoginExecption(String message, Integer code) {
        super(message, code);
    }

    public UserUnLoginExecption() {
    }
}
