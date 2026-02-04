package Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.yapeihraoumnbscxeqju";
    private static String PASSWORD;

    static {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
            PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            System.err.println("Database connection could not be loaded");
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
