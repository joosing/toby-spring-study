import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao{
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        final User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommand(rs.getInt("recommend"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(); // 인상적인 직접 생성, 수동 DI 장면이다.
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)",
                            user.getId(), user.getName(), user.getPassword(),
                            user.getLevel().intValue(), user.getLogin(), user.getRecommand()
        );
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject("select * from users where id = ?", userMapper, id);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("""
                                update users set name = ?, password = ?, level = ?,
                                login = ?, recommend = ? where id = ?""",
                                user.getName(), user.getPassword(), user.getLevel().intValue(),
                                user.getLogin(), user.getRecommand(),
                                user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from users");
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}
