package sql;

@SuppressWarnings("ThrowsRuntimeException")
public interface SqlService {

    void loadSql();
    String getSql(String key) throws SqlRetrievalFailureException;
}
