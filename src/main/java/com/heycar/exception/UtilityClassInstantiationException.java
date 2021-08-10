package com.heycar.exception;

public class UtilityClassInstantiationException extends IllegalStateException {

    public UtilityClassInstantiationException() {
        super("I'M AN UTILITY CLASS, PLEASE DON'T MAKE AN INSTANCE OF ME");
    }
    
}
