package dbService.dao;

import dbService.DBException;
import dbService.User;

public interface UsersDAO {

    void insertUser(String login, String password) throws DBException;

    User getUserByLogin (String login) throws DBException;

}
