import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class UserDaoTest {
    @Autowired
    private UserDao dao;
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() throws SQLException {
        // 테이블 초기화
        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        // User Fixture 생성
        user1 = new User("gyumee", "한주승", "hello", Level.BASIC, 1, 0, "joosing711@gmail.com");
        user2 = new User("leegw700", "헤이", "hey", Level.SILVER, 55, 10, "joosing711@gmail.com");
        user3 = new User("bumjin", "하이", "hi", Level.GOLD, 100, 40, "joosing711@gmail.com");
    }

    @Test
    public void addAndGet() throws Exception {
        dao.add(user1);
        dao.add(user2);
        Assert.assertEquals(2, dao.getCount());

        final User testUser = dao.get(user1.getId());
        Assert.assertEquals(user1.getName(), testUser.getName());
        Assert.assertEquals(user1.getId(), testUser.getId());

        final User testUser2 = dao.get(user2.getId());
        Assert.assertEquals(user2.getName(), testUser2.getName());
        Assert.assertEquals(user2.getId(), testUser2.getId());
    }

    @Test
    public void updateTest() {
        dao.add(user1); // 수정할 사용자
        dao.add(user2); // 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("hello2");
        user1.setLevel(Level.GOLD);
        user1.setLogin(999);
        user1.setRecommend(888);
        user1.setEmail("joosing711@naver.com");

        dao.update(user1);

        final User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        final User user2update = dao.get(user2.getId());
        checkSameUser(user2, user2update);
    }

    @Test
    public void getAll() throws Exception {
        final List<User> users0 = dao.getAll();
        Assert.assertEquals(0, users0.size());

        dao.add(user1);
        final List<User> users1 = dao.getAll();
        Assert.assertEquals(1, users1.size());
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        final List<User> users2 = dao.getAll();
        Assert.assertEquals(2, users2.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        final List<User> users3 = dao.getAll();
        Assert.assertEquals(3, users3.size());
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private static void checkSameUser(User user1, User user2) {
        Assert.assertEquals(user1.getId(), user2.getId());
        Assert.assertEquals(user1.getName(), user2.getName());
        Assert.assertEquals(user1.getPassword(), user2.getPassword());
        Assert.assertEquals(user1.getLevel(), user2.getLevel());
        Assert.assertEquals(user1.getLogin(), user2.getLogin());
        Assert.assertEquals(user1.getRecommend(), user2.getRecommend());
        Assert.assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void getCount() throws Exception {
        dao.add(user1);
        Assert.assertEquals(1, dao.getCount());

        dao.add(user2);
        Assert.assertEquals(2, dao.getCount());

        dao.add(user3);
        Assert.assertEquals(3, dao.getCount());
    }

    @Test
    public void getUserFailure() throws Exception {
        Assert.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicatedKey() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException ex) {
            final SQLException sqlEx = (SQLException) ex.getRootCause();
            final SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            assert sqlEx != null;
            Assert.assertTrue(set.translate(null, null, sqlEx) instanceof DuplicateKeyException);
        }
    }
}
