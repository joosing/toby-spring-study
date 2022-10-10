package service;

import org.springframework.mail.MailSender;

import dao.UserDao;
import pojo.User;

public interface UserLevelUpgradePolicy {
    void setMailSender(MailSender mailSender);
    void setUserDao(UserDao userDao);
    boolean can(User user);
    void upgrade(User user);
}
