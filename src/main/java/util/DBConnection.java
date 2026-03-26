package util;

import java.sql.Connection;
import java.sql.DriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {
    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);
    private static final String url = "jdbc:mysql://localhost:3306/employee_db";
    private static final String user = "root";
    private static final String password = "root123";

    public static Connection getConnection() {

        Connection con = null;
        try {
            log.debug("Loading MySQL JDBC Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            log.debug("Attempting to connect to database at: {}", url);
            con = DriverManager.getConnection(url, user, password);
            log.info("Database connection established successfully");
        } catch (Exception e) {
            log.error("Failed to establish database connection", e);
            e.printStackTrace();
        }
        return con;
    }
}