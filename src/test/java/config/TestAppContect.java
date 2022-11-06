package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dao.UserDao;
import service.UserService;
import service.UserServiceTest.TestUserLevelUpgradePolicy;
import service.UserServiceTest.TestUserServiceImpl;
import service.mock.DummyMailSender;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"dao", "service"})
@Profile("test")
public class TestAppContect {
    @Autowired
    UserDao userDao;

    @Bean
    public UserService testUserService() {
        TestUserServiceImpl userService = new TestUserServiceImpl();
        userService.setUserDao(userDao);
        userService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy());
        return userService;
    }

    @Bean
    public TestUserLevelUpgradePolicy testUserLevelUpgradePolicy() {
        TestUserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserLevelUpgradePolicy();
        userLevelUpgradePolicy.setUserDao(userDao);
        userLevelUpgradePolicy.setMailSender(mailSender());
        return userLevelUpgradePolicy;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
