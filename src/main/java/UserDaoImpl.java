import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDaoImpl extends UserDao {
    public UserDaoImpl() {
    }

    public UserDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement makeStatement(Connection c, String sql) throws SQLException {
        return c.prepareStatement(sql);
    }
}
