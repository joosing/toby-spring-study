package service;

import dao.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import pojo.User;
import proxy.TransactionProxyFactoryBean;
import service.mock.MockMailSender;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations="/applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
public class UserServiceTest {
    @Autowired
    ApplicationContext context;
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
        Assertions.assertEquals(levelBefore, user.getLevel());

        final User userRead = userDao.get(user.getId());
        Assertions.assertEquals(levelBefore, userRead.getLevel());
    }

    @Test
    public void addWithoutLevel() {
        final User user = users.get(0);
        user.setLevel(null);

        userService.add(user);
        Assertions.assertEquals(Level.BASIC, user.getLevel());

        final User userRead = userDao.get(user.getId());
        Assertions.assertEquals(Level.BASIC, userRead.getLevel());
    }

    @Test
    public void upgradeLevelsWithMockFramework() {
        // Given
        final UserServiceImpl testUserService = new UserServiceImpl();

        final UserLevelUpgradePolicy testUpgradePolicy = new GeneralUserLevelUpgradePolicy();
        testUserService.setUserLevelUpgradePolicy(testUpgradePolicy);

        final UserDao mockUserDao = Mockito.mock(UserDao.class); // 1. 스프링 빈이 아닌 경우에도 Mock 프레임워크 지원을 받을 수 있다.
        Mockito.when(mockUserDao.getAll()).thenReturn(users); // 2. Mock 객체의 반환 값을 의도한 값으로 고정하 수 있다.
        testUserService.setUserDao(mockUserDao);
        testUpgradePolicy.setUserDao(mockUserDao);

        final MailSender mockMailSender = Mockito.mock(MailSender.class);
        testUpgradePolicy.setMailSender(mockMailSender);

        // When
        testUserService.upgradeLevels();

        // Then
        Mockito.verify(mockUserDao, Mockito.times(2)).update(any(User.class)); // 3. 특정 타입 객체를 파라미터로 Mock 객체 메서드가 호출된 횟수 검증
        Mockito.verify(mockUserDao).update(users.get(1)); // 4. 특정 객체를 파라미터로 Mock 객체 메서드가 호출되었는지 검증
        Assert.assertEquals(Level.SILVER, users.get(1).getLevel());
        Mockito.verify(mockUserDao).update(users.get(3));
        Assert.assertEquals(Level.GOLD, users.get(3).getLevel());

        final ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mockMailSender, Mockito.times(2)).send(mailMessageArg.capture()); // 5. Mock 객체 메서드로 전달된 파라미터를 캡처해서 검증
        final List<SimpleMailMessage> allValues = mailMessageArg.getAllValues();
        Assert.assertEquals(users.get(1).getEmail(), Objects.requireNonNull(allValues.get(0).getTo())[0]);
        Assert.assertEquals(users.get(3).getEmail(), Objects.requireNonNull(allValues.get(1).getTo())[0]);
    }

    /**
     * 순수한 사용자 Level 업그레이드 기능을 테스트함으로 트랜잭션과 관련된 코드는 필요 없다. 테스트에서 관심을 가지는 부분에 완전히
     * 집중한 테스트 코드이다.
     */
    @Test
    public void upgradeLevelsWithManualMock() throws Exception {
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

    @SuppressWarnings("CatchMayIgnoreException")
    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        final TestUserLevelUpgradePolicy policy = new TestUserLevelUpgradePolicy(users.get(3).getId());
        policy.setUserDao(userDao);
        policy.setMailSender(mailSender);

        UserServiceImpl userServiceImpl = context.getBean("userServiceImpl", UserServiceImpl.class);
        userServiceImpl.setUserLevelUpgradePolicy(policy);

        TransactionProxyFactoryBean factoryBean = context.getBean("&userService", TransactionProxyFactoryBean.class);
        factoryBean.setTarget(userServiceImpl);
        UserService testUserService = (UserService) factoryBean.getObject();

        users.forEach(user -> userDao.add(user));

        try {
            Objects.requireNonNull(testUserService).upgradeLevels();
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
