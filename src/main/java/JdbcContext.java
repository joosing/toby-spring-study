import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(final String sql) throws SQLException {
        workWithStatementStrategy(connection -> connection.prepareStatement(sql));
    }

    public void executeSql(final String sql, String...args) throws SQLException {
        workWithStatementStrategy(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setString(i + 1, args[i]);
            }
            return preparedStatement;
        });
    }

    private void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = strategy.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            // 여기서 예외를 던질 필요가 없는지 체크하세요.
            throw e;
        } finally {
            if (ps != null) {
                try { // 아래에 있는 연결에 대한 close()를 위해 꼭 필요함
                    ps.close();
                } catch (SQLException ignored) {}
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
