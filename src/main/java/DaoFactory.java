import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    public UserDao userDao() {
        final ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public ConnectionMaker connectionMaker() {
        return new SimpleConnectionMaker();
    }
}
