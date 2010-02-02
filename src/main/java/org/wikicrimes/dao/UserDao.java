package org.wikicrimes.dao;

import java.util.List;

import org.wikicrimes.model.User;


public interface UserDao extends Dao {
    public List getUsers();
    public User getUser(Long userId);
    public void saveUser(User user);
    public void removeUser(Long userId);
}
