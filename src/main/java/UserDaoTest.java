import java.sql.SQLException;

public final class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        final UserDao dao = new DaoFactory().userDao();

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
