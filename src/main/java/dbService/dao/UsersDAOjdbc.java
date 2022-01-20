package dbService.dao;

import dbService.DBException;
import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;
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

    public UsersDataSet get(long id) throws DBException {
        try {
            return executor.execQuery("select * from users where id=" + id, result -> {
                result.next();
                UsersDataSet user = new UsersDataSet();
                user.setId(result.getLong(1));
                user.setLogin(result.getString(2));
                user.setPassword(result.getString(3));
                return user;
            });
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public long getUserId(String login) throws SQLException {
        return executor.execQuery("select * from users where login='" + login + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    @Override
    public void insertUser(String login, String password) throws DBException {
        try {
            connection.setAutoCommit(false);
            executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), " +
                    "password varchar(256), primary key (id))");
            executor.execUpdate("insert into users (login, password) values ('" + login + "', '" + password + "')");
            connection.commit();
            getUserId(login);
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
    public UsersDataSet getUserByLogin(String login) throws DBException {
        try {
            return get(getUserId(login));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
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
