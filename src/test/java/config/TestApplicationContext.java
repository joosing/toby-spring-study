package config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dao.UserDao;
import dao.UserDaoJdbc;
import service.GeneralUserLevelUpgradePolicy;
import service.UserService;
import service.UserServiceImpl;
import service.UserServiceTest.TestUserLevelUpgradePolicy;
import service.UserServiceTest.TestUserServiceImpl;
import service.mock.DummyMailSender;
import sql.EmbeddedDbSqlRegistry;
import sql.OxmSqlService;
import sql.SqlRegistry;
import sql.SqlService;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl("jdbc:postgresql://localhost:5432/toby-spring");
        dataSource.setUsername("follower");
        dataSource.setPassword("hello");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        userDao.setSqlService(sqlService());
        return userDao;
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
        return userService;
    }

    @Bean
    public GeneralUserLevelUpgradePolicy userLevelUpgradePolicy() {
        GeneralUserLevelUpgradePolicy userLevelUpgradePolicy = new GeneralUserLevelUpgradePolicy();
        userLevelUpgradePolicy.setUserDao(userDao());
        userLevelUpgradePolicy.setMailSender(mailSender());
        return userLevelUpgradePolicy;
    }

    @Bean
    public UserService testUserService() {
        TestUserServiceImpl userService = new TestUserServiceImpl();
        userService.setUserDao(userDao());
        userService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy());
        return userService;
    }

    @Bean
    public TestUserLevelUpgradePolicy testUserLevelUpgradePolicy() {
        TestUserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserLevelUpgradePolicy();
        userLevelUpgradePolicy.setUserDao(userDao());
        userLevelUpgradePolicy.setMailSender(mailSender());
        return userLevelUpgradePolicy;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.loadSql();
        return sqlService;
    }

    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/sql/sqlRegistrySchema.sql")
                .build();
    }

    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("sql.jaxb");
        return marshaller;
    }
}
