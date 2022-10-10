package service;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import dao.UserDao;
import pojo.User;
import service.mock.MockMailSender;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    @Autowired
    UserDao userDao;
    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    MailSender mailSender;

    List<User> users; // 테스트 픽스처

    @Before
    public void setUp() {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("bumjini", "박범진", "p1", Level.BASIC, GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER - 1, 0, "joosing711@gmail.com"),
                new User("joytouch", "강명성", "p2", Level.BASIC, GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER, 0, "joosing711@gmail.com"),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, GeneralUserLevelUpgradePolicy.MIN_RECOMMEND_COUNT_FOR_GOLD-1, "joosing711@gmail.com"),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, GeneralUserLevelUpgradePolicy.MIN_RECOMMEND_COUNT_FOR_GOLD, "joosing711@gmail.com"),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "joosing711@gmail.com")
        );
    }

    @Test
    public void addWithLevel() {
        final User user = users.get(4);

        final Level levelBefore = user.getLevel();
        userService.add(user);
        Assert.assertEquals(levelBefore, user.getLevel());

        final User userRead = userDao.get(user.getId());
        Assert.assertEquals(levelBefore, userRead.getLevel());
    }

    @Test
    public void addWithoutLevel() {
        final User user = users.get(0);
        user.setLevel(null);

        userService.add(user);
        Assert.assertEquals(Level.BASIC, user.getLevel());

        final User userRead = userDao.get(user.getId());
        Assert.assertEquals(Level.BASIC, userRead.getLevel());
    }

    @Test
    public void upgradeLevels() throws Exception {
        // Given
        final UserServiceImpl testUserService = new UserServiceImpl();

        final UserLevelUpgradePolicy testUserLevelUpgradePolicy = new GeneralUserLevelUpgradePolicy();
        testUserService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy);

        final MockUserDao mockUserDao = new MockUserDao(users);
        testUserService.setUserDao(mockUserDao);
        testUserLevelUpgradePolicy.setUserDao(mockUserDao);

        final MockMailSender mockMailSender = new MockMailSender();
        testUserLevelUpgradePolicy.setMailSender(mockMailSender);

        // When
        testUserService.upgradeLevels();

        // Then
        final List<String> mailRequests = mockMailSender.getRequests();
        Assert.assertEquals(2, mailRequests.size());
        Assert.assertEquals(users.get(1).getEmail(), mailRequests.get(0));
        Assert.assertEquals(users.get(3).getEmail(), mailRequests.get(1));

        final List<User> updateRequests = mockUserDao.getUpdated();
        Assert.assertEquals(2, updateRequests.size());
        checkUserAndLevel(updateRequests.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updateRequests.get(1), "madnite1", Level.GOLD);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        final TestUserLevelUpgradePolicy policy = new TestUserLevelUpgradePolicy(users.get(3).getId());
        policy.setUserDao(userDao);
        policy.setMailSender(mailSender);

        final UserServiceImpl testUserServiceImpl = new UserServiceImpl();
        testUserServiceImpl.setUserDao(userDao);
        testUserServiceImpl.setUserLevelUpgradePolicy(policy);

        final UserServiceTx txUserService = new UserServiceTx();
        txUserService.setUserService(testUserServiceImpl);
        txUserService.setTransactionManager(transactionManager);

        users.forEach(user -> userDao.add(user));

        try {
            txUserService.upgradeLevels();
            Assert.fail("TestUserServiceException expected");
        } catch (TestUserServiceException ex) {

        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        final User userUpdated = userDao.get(user.getId());
        if (upgraded) {
            Assert.assertEquals(user.getLevel().nextLevel(), userUpdated.getLevel());
        } else {
            Assert.assertEquals(user.getLevel(), userUpdated.getLevel());
        }
    }

    private static void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        Assert.assertEquals(expectedId, updated.getId());
        Assert.assertEquals(expectedLevel, updated.getLevel());
    }

    static class TestUserLevelUpgradePolicy extends GeneralUserLevelUpgradePolicy {
        private final String id;

        TestUserLevelUpgradePolicy(String id) {
            this.id = id;
        }

        @Override
        public void upgrade(User user) {
            if (user.getId().equals(id)) {throw new TestUserServiceException();}
            super.upgrade(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = -1962780078733550838L;
    }


    static final class MockUserDao implements UserDao{
        final List<User> users;
        final List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}
