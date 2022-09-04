import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTestWithXml {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        final ApplicationContext applicationContext =
                new GenericXmlApplicationContext("applicationContext.xml");
        final UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        final User user = new User();
        user.setId("JooSing");
        user.setName("주승");
        user.setPassword("hello");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        final User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
