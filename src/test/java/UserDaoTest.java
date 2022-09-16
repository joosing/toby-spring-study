import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        final ApplicationContext applicationContext =
                new GenericXmlApplicationContext("applicationContext.xml");
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        dao.deleteAll();
        Assert.assertEquals(0, dao.getCount());

        final User expectedUser = new User();
        expectedUser.setId("JooSing");
        expectedUser.setName("주승");
        expectedUser.setPassword("hello");

        dao.add(expectedUser);
        Assert.assertEquals(1, dao.getCount());

        final User testUser = dao.get(expectedUser.getId());
        Assert.assertEquals(expectedUser.getName(), testUser.getName());
        Assert.assertEquals(expectedUser.getId(), testUser.getId());
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
}
