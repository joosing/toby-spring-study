import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        final ApplicationContext applicationContext =
                new GenericXmlApplicationContext("applicationContext.xml");
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        final User expectedUser1 = new User("hi", "한주승", "hi");
        final User expectedUser2 = new User("hey", "헤이", "hey");

        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

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
        final ApplicationContext applicationContext =
                new GenericXmlApplicationContext("applicationContext.xml");
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        final User expectedUser1 = new User("joosing", "한주승", "hello");
        final User expectedUser2 = new User("hey", "헤이", "hey");
        final User expectedUser3 = new User("hi", "하이", "hi");

        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        dao.add(expectedUser1);
        Assert.assertEquals(1, dao.getCount());

        dao.add(expectedUser2);
        Assert.assertEquals(2, dao.getCount());

        dao.add(expectedUser3);
        Assert.assertEquals(3, dao.getCount());
    }

    @Test
    public void getUserFailure() throws Exception {
        final ApplicationContext applicationContext =
                new GenericXmlApplicationContext("applicationContext.xml");
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        Assert.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }
}
