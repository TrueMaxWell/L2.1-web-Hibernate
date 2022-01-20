package dbService.dao;

import dbService.DBException;
import dbService.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;

public class UsersDAOjdbc implements UsersDAO {

    private final Connection connection;

    public UsersDAOjdbc() {
        this.connection = getH2Connection();
    }

    @Override
    public void insertUser(String login, String password) throws DBException {
        try {
            connection.setAutoCommit(false);

            Statement stmt = connection.createStatement();
            stmt.execute("create table if not exists users (id bigint auto_increment, login varchar(256), " +
                    "password varchar(256), primary key (id))");
            stmt.close();

            PreparedStatement addUser =
                    connection.prepareStatement("insert into users (login, password) values (?,?)");
            addUser.setString(1,login);
            addUser.setString(2, password);
            addUser.execute();
            addUser.close();
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

            PreparedStatement statement = connection.prepareStatement("select * from users where login= ?");
            statement.setString(1,login);
            statement.executeQuery();
            ResultSet result = statement.getResultSet();

            if (result.next()) {
                User user = new User();
                user.setId(result.getLong(1));
                user.setLogin(result.getString(2));
                user.setPassword(result.getString(3));
                result.close();
                statement.close();
                return user;
            } else {
                return null;
            }


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
