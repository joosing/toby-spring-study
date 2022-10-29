package sql;

@FunctionalInterface
public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
