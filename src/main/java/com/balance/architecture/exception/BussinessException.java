package com.balance.architecture.exception;

public class BussinessException extends Exception {
    public BussinessException(){
        super();
    }

    public BussinessException(String message){
        super(message);
    }
}
