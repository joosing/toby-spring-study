import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
    @Test
    public void addAndGet() throws Exception {
        final ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        final User user = new User();
        user.setId("JooSing");
        user.setName("주승");
        user.setPassword("hello");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());
        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println(user2.getId() + " 조회 성공");
        }
    }
}
