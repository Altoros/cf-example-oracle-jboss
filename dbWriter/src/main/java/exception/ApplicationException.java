package exception;

import org.apache.log4j.Logger;

public class ApplicationException extends RuntimeException {

    private static final Logger logger = Logger.getRootLogger();
    private static final long serialVersionUID = 2178675741248911603L;

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }

    public static RuntimeException fail(Throwable cause) {
        throw new ApplicationException(cause);
    }

    public static RuntimeException fail(String message) {
        logger.error(message);
        throw new ApplicationException(message);
    }

    public static RuntimeException fail(String message, Throwable cause) {
        logger.error(message);
        logger.error(cause);
        throw new ApplicationException(message, cause);
    }
}
