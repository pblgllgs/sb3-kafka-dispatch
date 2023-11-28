package com.pblgllgs.exception;
/*
 *
 * @author pblgl
 * Created on 28-11-2023
 *
 */

public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(Exception exception) {
        super(exception);
    }
}