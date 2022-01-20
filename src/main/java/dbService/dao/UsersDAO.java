package dbService.dao;

import dbService.DBException;
import dbService.dataSets.UsersDataSet;

import java.sql.SQLException;

public interface UsersDAO {

    void insertUser(String login, String password) throws DBException;

    UsersDataSet getUserByLogin (String login) throws DBException;

    void dropTable() throws SQLException;

}
