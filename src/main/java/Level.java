import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Level {
    BASIC(1), SILVER(2), GOLD(3);
    private final int level;

    public int intValue() {
        return level;
    }

    public static Level valueOf(int value) {
        return switch (value) {
            case 1 -> BASIC;
            case 2 -> SILVER;
            case 3 -> GOLD;
            default -> throw new AssertionError("Unknown value : " + value);
        };
    }
}
