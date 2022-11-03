package sql;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public abstract class AbstratUpdatableSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry;

    @Before
    public void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    protected abstract UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Test
    public void find() {
        checkFind("SQL1", "SQL2", "SQL3");
    }

    @Test(expected = SqlRetrievalFailureException.class)
    public void unknownKey() {
        sqlRegistry.findSql("SQL9999!@#$");
    }

    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFind("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1", "Modified1");
        sqlMap.put("KEY2", "Modified2");

        sqlRegistry.updateSql(sqlMap);
        checkFind("Modified1", "Modified2", "SQL3");
    }

    @Test(expected = SqlRetrievalFailureException.class)
    public void updateWithNotExistingKey() {
        sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
    }

    protected void checkFind(String expected1, String expected2, String expected3) {
        assertEquals(expected1, sqlRegistry.findSql("KEY1"));
        assertEquals(expected2, sqlRegistry.findSql("KEY2"));
        assertEquals(expected3, sqlRegistry.findSql("KEY3"));
    }
}
