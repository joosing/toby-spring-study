public class DaoFactory {
    public UserDao userDao() {
        final ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    private ConnectionMaker connectionMaker() {
        return new SimpleConnectionMaker();
    }
}
