package sql;

import java.io.Serial;

public class SqlRetrievalFailureException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4411008427154074018L;

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
