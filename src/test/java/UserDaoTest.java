import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class UserDaoTest {
    private UserDao dao;

    private User expectedUser1;
    private User expectedUser2;
    private User expectedUser3;

    @Before
    public void setUp() throws SQLException {
        dao = new UserDao();
        final DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:postgresql://localhost:5432/testdb", "follower", "hello", true);
        dao.setDataSource(dataSource);

        // 테이블 초기화
        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        // User Fixture 생성
        expectedUser1 = new User("hello", "한주승", "hello");
        expectedUser2 = new User("hey", "헤이", "hey");
        expectedUser3 = new User("hi", "하이", "hi");
    }

    @Test
    public void addAndGet() throws Exception {
        dao.add(expectedUser1);
        dao.add(expectedUser2);
        Assert.assertEquals(2, dao.getCount());

        final User testUser = dao.get(expectedUser1.getId());
        Assert.assertEquals(expectedUser1.getName(), testUser.getName());
        Assert.assertEquals(expectedUser1.getId(), testUser.getId());

        final User testUser2 = dao.get(expectedUser2.getId());
        Assert.assertEquals(expectedUser2.getName(), testUser2.getName());
        Assert.assertEquals(expectedUser2.getId(), testUser2.getId());
    }

    @Test
    public void getCount() throws Exception {
        dao.add(expectedUser1);
        Assert.assertEquals(1, dao.getCount());

        dao.add(expectedUser2);
        Assert.assertEquals(2, dao.getCount());

        dao.add(expectedUser3);
        Assert.assertEquals(3, dao.getCount());
    }

    @Test
    public void getUserFailure() throws Exception {
        Assert.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }
}
