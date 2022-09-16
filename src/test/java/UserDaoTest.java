import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        final ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(DaoFactory.class);
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
}
