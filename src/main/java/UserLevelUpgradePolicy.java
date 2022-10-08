public interface UserLevelUpgradePolicy {
    boolean can(User user);
    void upgrade(User user);
}
