import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;

    public void upgradeLevel() {
        final Level nextLevel = level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(level + "은 업그레이드가 불가능합니다.");
        } else {
            level = nextLevel;
        }
    }
}
