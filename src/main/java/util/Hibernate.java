package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {

    private static SessionFactory sessionFactory;
    // static block is used to run this exactly once to to ensure it doesn't runn
    // everytime else it will crash the mysqlServer
    static {
        try {

            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to return session for use in DAO
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}