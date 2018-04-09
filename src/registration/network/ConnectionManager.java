package registration.network;

import registration.models.Model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Raniel on 10/9/2017.
 */
public class ConnectionManager {

    public static Connection con;

    public boolean connectToDatabase(String url, String username, String password, String database_name) {
        boolean access;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, username, password);
            access = true;
            con.setCatalog(database_name);

            Model.setConnection(con);

        } catch (Exception error) {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, "Can't connect to database",
                    "Error to connect...", JOptionPane.ERROR_MESSAGE);
            access = false;
        }

        return access;
    }

    public static Connection getConnection() {
        return con;
    }

}
