package dev.stroe.buggloo.exceptions;

/**
 * Exception thrown when service operations fail.
 */
public class ServiceException extends RuntimeException {
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
} 