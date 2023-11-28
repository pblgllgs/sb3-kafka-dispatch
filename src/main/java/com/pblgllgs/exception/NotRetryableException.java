package com.pblgllgs.exception;
/*
 *
 * @author pblgl
 * Created on 28-11-2023
 *
 */

public class NotRetryableException extends RuntimeException {

    public NotRetryableException(Exception exception) {
        super(exception);
    }
}