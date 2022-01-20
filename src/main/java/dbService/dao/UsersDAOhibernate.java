package dbService.dao;

import dbService.DBException;
import dbService.User;
import dbService.hibernateSessionFactory.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsersDAOhibernate implements UsersDAO {

    Session session = SessionFactory.getSessionFactory().openSession();

    @Override
    public void insertUser(String login, String password) throws DBException {

        Transaction transaction = session.beginTransaction();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        session.saveOrUpdate(user);
        transaction.commit();
    }

    @Override
    public User getUserByLogin(String login) throws DBException {

        Query query = session.createQuery("from User where login = :login");
        query.setParameter("login", login);
        User user = (User) query.uniqueResult();
        return user;
    }
}
