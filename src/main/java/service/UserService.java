package service;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import dao.UserDao;
import pojo.User;

public class UserService {
    private UserDao userDao;
    private PlatformTransactionManager transactionManager;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void upgradeLevels() throws Exception {
        final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            upgradeLevelsInternal();
            transactionManager.commit(status);
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }

    private void upgradeLevelsInternal() throws Exception {
        final List<User> users = userDao.getAll();
        for (User user : users) {
            if (userLevelUpgradePolicy.can(user)) {
                userLevelUpgradePolicy.upgrade(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
