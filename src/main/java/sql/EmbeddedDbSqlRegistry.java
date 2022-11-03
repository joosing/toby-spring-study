package sql;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

@SuppressWarnings("SqlResolve")
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbcTemplate.update("insert into SQLMAP(KEY_, SQL_) values(?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalFailureException {
        try {
            return jdbcTemplate.queryForObject("select SQL_ from SQLMAP where KEY_= ?", String.class, key);
        } catch (EmptyResultDataAccessException ex) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다", ex);
        }
    }

    @Override
    public void updateSql(String key, String sql) throws SqlRetrievalFailureException {
        int affected = jdbcTemplate.update("update SQLMAP set SQL_ = ? where KEY_ = ?", sql, key);
        if (affected == 0 ) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlRetrievalFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
