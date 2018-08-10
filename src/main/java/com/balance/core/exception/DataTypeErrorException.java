package com.balance.core.exception;

public class DataTypeErrorException extends Exception{
    public DataTypeErrorException(){
        super();
    }

    public DataTypeErrorException(String message){
        super(message);
    }
}
