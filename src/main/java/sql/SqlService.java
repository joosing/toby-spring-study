package sql;

@SuppressWarnings("ThrowsRuntimeException")
@FunctionalInterface
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
