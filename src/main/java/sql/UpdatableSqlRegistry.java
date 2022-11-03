package sql;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlRetrievalFailureException;
    void updateSql(Map<String, String> sqlmap) throws SqlRetrievalFailureException;
}
