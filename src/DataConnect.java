import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DataConnect {
    private static Connection instance = null;
    private Properties props = new Properties();

    /**
     * Connect to the database
     * @param frame
     */
    private DataConnect(MainGUI frame) {
        FileInputStream in;
        try {
            in = new FileInputStream("./db.props");
            props.load(in);
            in.close();

            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            String schema = props.getProperty("jdbc.schema");

            instance = DriverManager.getConnection(url + "/" + schema, username,
                    password);

            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    disconnect();
                }
            });
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Disconnect to the database
     */
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

    /**
     * Connect to the database
     * @param frame MainGUI frame
     * @return connection instance
     */
    public static Connection getInstance(MainGUI frame) {
        if (instance == null) {
            new DataConnect(frame);
        }
        return instance;
    }
}