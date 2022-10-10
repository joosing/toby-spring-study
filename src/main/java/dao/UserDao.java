package dao;

import java.util.List;

import pojo.User;

public interface UserDao {
    void add(User user);
    User get(String id);
    void update(User user);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
