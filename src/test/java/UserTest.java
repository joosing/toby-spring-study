import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        final Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
            Assert.assertEquals(level.nextLevel(), user.getLevel());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        final Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
