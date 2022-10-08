import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class UserService {
    private DataSource dataSource;
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        final Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            final List<User> users = userDao.getAll();
            for (User user : users) {
                if (userLevelUpgradePolicy.can(user)) {
                    userLevelUpgradePolicy.upgrade(user);
                }
            }
            c.commit();
        } catch (Exception ex) {
            c.rollback();
            throw ex;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }

    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
