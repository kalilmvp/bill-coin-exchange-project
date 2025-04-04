package com.kmvp.billcoinexhange.app.exceptions;

/**
 * @author kalil.peixoto
 * @date 4/2/25 20:01
 * @email kalilmvp@gmail.com
 */
public class NotEnoughCoinsException extends RuntimeException{
    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
