package com.passcom.PassCom.exceptions;

public class TravelNotFoundException extends RuntimeException{
    public TravelNotFoundException(String message){
        super(message);
    }
}
