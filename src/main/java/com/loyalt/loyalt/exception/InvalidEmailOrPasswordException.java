package com.loyalt.loyalt.exception;

public class InvalidEmailOrPasswordException extends RuntimeException{
    public InvalidEmailOrPasswordException(String message){
        super(message);
    }
}
