package chao.app.ami;

/**
 * @author chao.qin
 * @since 2017/7/31
 */

public class AmiException extends RuntimeException {
    public AmiException(){

    }

    public AmiException(String message) {
        super(message);
    }

    public AmiException(String message, Throwable e) {
        super(message, e);
    }
}
