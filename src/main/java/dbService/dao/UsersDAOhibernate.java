package dbService.dao;

import dbService.DBException;
import dbService.dataSets.UsersDataSet;
import dbService.sessionFactory.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsersDAOhibernate implements UsersDAO {

    Session session = HibernateSessionFactory.getSessionFactory().openSession();

    @Override
    public void insertUser(String login, String password) throws DBException {

        Transaction transaction = session.beginTransaction();
        UsersDataSet user = new UsersDataSet();
        user.setLogin(login);
        user.setPassword(password);
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    @Override
    public UsersDataSet getUserByLogin(String login) throws DBException {

        Query query = session.createQuery("from UsersDataSet where login = :login");
        query.setParameter("login", login);
        UsersDataSet user = (UsersDataSet) query.uniqueResult();
        session.close();
        return user;
    }

    @Override
    public void dropTable() {
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM UsersDataSet");
        transaction.commit();
        session.close();
    }
}
