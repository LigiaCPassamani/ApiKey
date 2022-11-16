package com.apikey.apikey.config;

/**
 * This class is for custom NullPointerException
 */
public class NullPointerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NullPointerException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
