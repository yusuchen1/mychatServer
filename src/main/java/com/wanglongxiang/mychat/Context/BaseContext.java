package com.wanglongxiang.mychat.Context;

public class BaseContext {
    public static ThreadLocal<Long>threadLocal = new ThreadLocal<>();
    public static void setContext(Long id){
        threadLocal.set(id);
    }
    public static Long getContext(){
        return threadLocal.get();
    }
    public static void removeContext(){
        threadLocal.remove();
    }
}
