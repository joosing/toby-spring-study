package study.oxm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import sql.jaxb.SqlType;
import sql.jaxb.Sqlmap;

public class JaxbTest {
    @Test
    public void readSqlMap() throws JAXBException {
        String contextPath = Sqlmap.class.getPackage().getName(); // 바인딩용 클래스들 위치를 가지고 JAXB 컨텍스트를 만든다.
        JAXBContext context = JAXBContext.newInstance(contextPath);
        Unmarshaller unmarshaller = context.createUnmarshaller(); // JAXBContext로부터 언마샬러를 만든다.

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                getClass().getResourceAsStream("/testSqlMap.xml")); // 언마샬러로 XML 파일을 객체로 변환한다.

        List<SqlType> sqlList = sqlmap.getSql();

        assertEquals(3, sqlList.size());
        assertEquals("add", sqlList.get(0).getKey());
        assertEquals("insertSql", sqlList.get(0).getValue());
        assertEquals("get", sqlList.get(1).getKey());
        assertEquals("selectSql", sqlList.get(1).getValue());
        assertEquals("delete", sqlList.get(2).getKey());
        assertEquals("deleteSql", sqlList.get(2).getValue());
    }
}
