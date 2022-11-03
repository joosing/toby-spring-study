package study.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SqlResolve")
public class EmbeddedDbTest {
    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/study/sql/schema.sql")
                .addScript("classpath:/study/sql/data.sql")
                .build();
        template = new JdbcTemplate(db);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData() {
        assertEquals(2, template.queryForObject("select count(*) from SQLMAP", Integer.class));
        List<Map<String, Object>> list = template.queryForList("select * from SQLMAP order by KEY_");
        assertEquals("KEY1", list.get(0).get("KEY_"));
        assertEquals("SQL1", list.get(0).get("SQL_"));
        assertEquals("KEY2", list.get(1).get("KEY_"));
        assertEquals("SQL2", list.get(1).get("SQL_"));
    }

    @Test
    public void insert() {
        template.update("insert into SQLMAP(KEY_, SQL_) values(?, ?)", "KEY3", "SQL3");
        assertEquals(3, template.queryForObject("select count(*) from SQLMAP", Integer.class));
    }
}
