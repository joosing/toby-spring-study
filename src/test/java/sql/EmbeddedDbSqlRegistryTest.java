package sql;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class EmbeddedDbSqlRegistryTest extends AbstratUpdatableSqlRegistryTest {
    EmbeddedDatabase db;
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/sql/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @SuppressWarnings("CatchMayIgnoreException")
    @Test
    public void transactionalUpdate() {
        checkFind("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modifeid1");
        sqlmap.put("KEY9999!@#$", "Modifeid9999"); // 업데이트에 실패하는 케이스 만들기

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlRetrievalFailureException exception) {}
        checkFind("SQL1", "SQL2", "SQL3");
    }
}
