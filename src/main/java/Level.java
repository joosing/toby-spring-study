import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Level {
    // 아래 순서가 틀리면 Illegal forward Reference 오류가 발생합니다.
    // 이는 객체가 선언되기 전에 사용되었기 때문입니다.
    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER),
    ;

    private final int level;
    private final Level next;

    public int intValue() {
        return level;
    }

    public Level nextLevel() {
        return next;
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
