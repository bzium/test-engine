package pl.plagodzinski.testengine.api;

public class ValidateException extends Exception {

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable e) {
        super(message, e);
    }


}
