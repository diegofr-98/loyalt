package com.loyalt.loyalt.exception.wallet;

public class GoogleWalletException extends RuntimeException{
    public GoogleWalletException(String message, Throwable cause){
        super(message, cause);

    }

    public GoogleWalletException(String message){
        super(message);

    }
}
