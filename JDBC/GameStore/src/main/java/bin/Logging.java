package bin;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Logging {

    public Logging(){}

    public void addLog(String text) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        String newLog = "INSERT INTO logs (user, action, date) VALUES (" + Main.id + ", " +
                "\"" + text +"\", (SELECT now()))";
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(newLog);

        ps.executeUpdate();
        ps.close();
    }

}
