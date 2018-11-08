package com.balance.architecture.exception;

public class StateErrorException extends Exception {
    public StateErrorException(){
        super();
    }

    public StateErrorException(String message){
        super(message);

    }
}
