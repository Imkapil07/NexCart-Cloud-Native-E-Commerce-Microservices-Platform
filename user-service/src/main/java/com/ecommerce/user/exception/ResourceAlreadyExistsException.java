package com.ecommerce.user.exception;

/**
 * Thrown when a resource (e.g. user email) already exists
 * (e.g. duplicate registration).
 **/
public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }
}