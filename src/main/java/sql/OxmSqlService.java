package sql;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import sql.jaxb.SqlType;
import sql.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader sqlReader= new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        sqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmap(Resource sqlmap) {
        sqlReader.setSqlmap(sqlmap);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    @Override
    public void loadSql() {
        baseSqlService.setSqlReader(sqlReader);
        baseSqlService.setSqlRegistry(sqlRegistry);

        baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return baseSqlService.getSql(key);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class OxmSqlReader implements SqlReader {
        private Unmarshaller unmarshaller;
        private Resource sqlmap = new ClassPathResource("/oxm/sqlmap.xml"); // 디폴트

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
           try {
               Source source = new StreamSource(sqlmap.getInputStream());
               Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);
               for (SqlType sql : sqlmap.getSql()) {
                   sqlRegistry.registerSql(sql.getKey(), sql.getValue());
               }
           } catch (IOException e) {
               throw new IllegalArgumentException(sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
           }
        }
    }
}
