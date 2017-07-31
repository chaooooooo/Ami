package chao.app.ami;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class AMiException extends RuntimeException {
    public AMiException(){

    }

    public AMiException(String message) {
        super(message);
    }

    public AMiException(String message, Throwable e) {
        super(message, e);
    }
}
