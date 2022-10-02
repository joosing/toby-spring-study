import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class UserDaoTest {
    @Autowired
    private UserDao dao;

    private User expectedUser1;
    private User expectedUser2;
    private User expectedUser3;

    @Before
    public void setUp() throws SQLException {
        // 테이블 초기화
        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        // User Fixture 생성
        expectedUser1 = new User("gyumee", "한주승", "hello");
        expectedUser2 = new User("leegw700", "헤이", "hey");
        expectedUser3 = new User("bumjin", "하이", "hi");
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
    public void getAll() throws Exception {
        final List<User> users0 = dao.getAll();
        Assert.assertEquals(0, users0.size());

        dao.add(expectedUser1);
        final List<User> users1 = dao.getAll();
        Assert.assertEquals(1, users1.size());
        checkSameUser(expectedUser1, users1.get(0));

        dao.add(expectedUser2);
        final List<User> users2 = dao.getAll();
        Assert.assertEquals(2, users2.size());
        checkSameUser(expectedUser1, users2.get(0));
        checkSameUser(expectedUser2, users2.get(1));

        dao.add(expectedUser3);
        final List<User> users3 = dao.getAll();
        Assert.assertEquals(3, users3.size());
        checkSameUser(expectedUser3, users3.get(0));
        checkSameUser(expectedUser1, users3.get(1));
        checkSameUser(expectedUser2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        Assert.assertEquals(user1.getId(), user2.getId());
        Assert.assertEquals(user1.getName(), user2.getName());
        Assert.assertEquals(user1.getPassword(), user2.getPassword());
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
