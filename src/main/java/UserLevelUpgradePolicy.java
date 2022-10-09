import org.springframework.mail.MailSender;

public interface UserLevelUpgradePolicy {
    void setMailSender(MailSender mailSender);
    void setUserDao(UserDao userDao);
    boolean can(User user);
    void upgrade(User user);
}
