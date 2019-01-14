package bin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {

    private static final String HOST = "jdbc:mysql://localhost:3306/GameStore";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "050374";

    private Connection connection;

    public DBWorker() {
        try {
            connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
