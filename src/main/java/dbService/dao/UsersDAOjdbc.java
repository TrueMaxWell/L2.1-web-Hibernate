package dbService.dao;

import dbService.DBException;
import dbService.User;
import dbService.jdbcExecutor.Executor;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UsersDAOjdbc implements UsersDAO {

    private final Connection connection;
    private final Executor executor;

    public UsersDAOjdbc() {
        this.connection = getH2Connection();
        this.executor = new Executor(connection);
    }

    @Override
    public void insertUser(String login, String password) throws DBException {
        try {
            connection.setAutoCommit(false);
            executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), " +
                    "password varchar(256), primary key (id))");
            executor.execUpdate("insert into users (login, password) values ('" + login + "', '" + password + "')");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            throw new DBException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }

    }

    @Override
    public User getUserByLogin(String login) throws DBException {

        try {
            return executor.execQuery("select * from users where login='" + login + "'", result -> {
                result.next();
                User user = new User();
                user.setId(result.getLong(1));
                user.setLogin(result.getString(2));
                user.setPassword(result.getString(3));
                return user;
            });
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./h2db";
            String login = "test";
            String pass = "test";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(login);
            ds.setPassword(pass);

            return DriverManager.getConnection(url, login, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
