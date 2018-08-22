package com.test.mqcore.exception;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 4916104330691433027L;
    protected int code;

    public CustomException(String message) {
        super(message);
        this.code = -1;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(Throwable ex) {
        super(ex);
        this.code = -1;
    }

    public int getCode() {
        return this.code;
    }
}
