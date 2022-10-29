package sql;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Unmarshaller;

import sql.jaxb.SqlType;
import sql.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {
    private final OxmSqlReader sqlReader= new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        sqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        sqlReader.setSqlmapFile(sqlmapFile);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class OxmSqlReader implements SqlReader {
        private Unmarshaller unmarshaller;
        private static final String DEFAULT_MAP_FILE = "sqlmap.xml";
        private String sqlmapFile = DEFAULT_MAP_FILE;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
           try {
               Source source = new StreamSource(getClass().getResourceAsStream("/oxm/" + sqlmapFile));
               Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);
               for (SqlType sql : sqlmap.getSql()) {
                   sqlRegistry.registerSql(sql.getKey(), sql.getValue());
               }
           } catch (IOException e) {
               throw new IllegalArgumentException(sqlmapFile + "을 가져올 수 없습니다.", e);
           }
        }
    }

    @PostConstruct
    @Override
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return sqlRegistry.findSql(key);
    }
}
