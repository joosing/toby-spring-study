package dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pojo.User;
import service.Level;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao{
    private JdbcTemplate jdbcTemplate;
    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        final User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(); // 인상적인 직접 생성, 수동 DI 장면이다.
        jdbcTemplate.setDataSource(dataSource);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(sqlMap.get("add"),
                            user.getId(), user.getName(), user.getPassword(),
                            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail()
        );
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlMap.get("get"), userMapper, id);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlMap.get("update"),
                user.getName(), user.getPassword(), user.getLevel().intValue(),
                user.getLogin(), user.getRecommend(), user.getEmail(),
                user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(sqlMap.get("getAll"), userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(sqlMap.get("deleteAll"));
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlMap.get("getCount"), Integer.class);
    }
}
