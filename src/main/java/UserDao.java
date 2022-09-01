import java.sql.SQLException;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return new User();
    }
}
