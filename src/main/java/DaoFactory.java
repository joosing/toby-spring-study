public class DaoFactory {
    public UserDao userDao() {
        final ConnectionMaker connectionMaker = new SimpleConnectionMaker();
        final UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }
}
