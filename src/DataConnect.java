import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.Properties;

public class DataConnect {
    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/mazeProgram";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String MAX_POOL = "250";

    // init connection object
    private static Connection instance = null;
    // init properties object
    private Properties properties;

    // connect database
    private DataConnect() {
        if (instance == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                instance = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // disconnect database
    public void disconnect() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DataConnect();
        }
        return instance;
    }
}