import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        final User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(); // 인상적인 직접 생성, 수동 DI 장면이다.
        jdbcTemplate.setDataSource(dataSource);
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
                               user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?", userMapper, id);
    }

    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", userMapper);
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}
