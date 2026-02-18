package com.ecommerce.user.exception;

/**
 * Thrown when a requested resource (e.g. user) is not found.
 **/
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}