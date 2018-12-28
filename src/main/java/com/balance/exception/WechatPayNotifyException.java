package com.balance.exception;

public class WechatPayNotifyException extends RuntimeException {
    public WechatPayNotifyException(){
        super();
    }

    public WechatPayNotifyException(String message){
        super(message);
    }
}