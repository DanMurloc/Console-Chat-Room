package org.neroimor.cm.Server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.neroimor.cm.ServerDb.HibernateUtil;
import org.neroimor.cm.ServerDb.User;

public class SinglDataBase {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static boolean addAndGetUser(String name) {
        Session session = null;
        User user = null;
        boolean result = false;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user = session.createQuery("FROM User WHERE name = :name", User.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (user == null) {
                user = new User();
                user.setName(name);
                user.setState(true);
                session.save(user);
                result = false;
            } else {
                user.setState(true);
                session.update(user);
                result = true;
            }
            session.getTransaction().commit();
        } finally {
            assert session != null;
            session.close();
        }
        return result;
    }

    public static void updateUser(String name) {
        Session session = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            user = session.createQuery("FROM User WHERE name = :name", User.class)
                    .setParameter("name", name)
                    .uniqueResult();
            user.setState(false);
            session.update(user);
            session.getTransaction().commit();
        } finally {
            assert session != null;
            session.close();
        }
    }

    public static void closeSession() {
        sessionFactory.close();
        HibernateUtil.shutdown();
    }
}
