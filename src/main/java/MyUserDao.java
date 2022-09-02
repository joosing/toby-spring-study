import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost/toby-spring", "follower", "hello");
    }
}
