package sql;

import java.io.Serial;

public class SqlNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4411008427154074018L;

    public SqlNotFoundException(String message) {
        super(message);
    }

    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
