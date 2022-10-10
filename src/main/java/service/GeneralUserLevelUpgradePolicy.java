package service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import dao.UserDao;
import pojo.User;

public class GeneralUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
    private UserDao userDao;
    private MailSender mailSender;

    @Override
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;

    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean can(User user) {
        return switch (user.getLevel()) {
            case BASIC -> user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
            case SILVER -> user.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD;
            case GOLD -> false;
            // default -> throw new IllegalArgumentException("Unknown service.Level: " + currentLevel);
            // 위 default 문은 새로운 Switch 문에서 불필요합니다. Enum 값이 추가되었는데 case 가 추가되지 않으면 컴파일 오류가 발생합니다.
        };
    }

    @Override
    public void upgrade(User user) {
        user.upgradeLevel(); // dto.User 객체의 레벨을 업그레이드 하고,
        userDao.update(user); // DB에 있는 dto.User 정보를 업그레이드 합니다. 와 간결하다!!!
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Subject");
        mailMessage.setText("Hello");

        mailSender.send(mailMessage);
    }
}
