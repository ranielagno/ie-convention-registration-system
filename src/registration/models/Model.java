package registration.models;

import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;

public class Model {

    private static final Model model = new Model();
    private static Connection connection;

    private Model() {
    }

    public static Model getInstance() {
        return model;
    }

    public static void setConnection(Connection connection) {
        Model.connection = connection;
    }

    public String insertParticipant(String lastName, String firstName, String middleName, String contactNumber,
                                  String gender, String studentNumber, String section, String paymentMethod) {

        /*
        System.out.println(lastName + " " + firstName + " " + middleName + " " + contactNumber + " " + gender
                + " " + studentNumber + " " + section + " " + paymentMethod);
                */

        String sql = "INSERT INTO `participants`(`first_name`, `middle_name`, `last_name`, `contact_number`, " +
                "`gender`, `student_number`, `section`, `mode_of_payment`) VALUES (" +
                "'" + firstName + "'," +
                "'" + middleName + "'," +
                "'" + lastName + "'," +
                "'" + contactNumber + "'," +
                "'" + gender + "'," +
                "'" + studentNumber + "'," +
                "'" + section + "'," +
                "'" + paymentMethod + "')";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            int id = 0;

            ResultSet res = statement.executeQuery("SELECT `id` FROM `participants` ORDER BY `id` DESC LIMIT 1");
            while (res.next()) {
                id = res.getInt("id");
            }

            DecimalFormat df = new DecimalFormat("0000");
            String format = df.format(id);
            String event_id = "PUPIE-" + format;

            statement.executeUpdate("UPDATE `participants` SET event_id = '" + event_id + "' WHERE `id` = " + id);
            res.close();

            return event_id;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't connect to database",
                    "Error to connect...", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return "";
    }

    public boolean addFingerprintData(byte[] data, String event_id) {

        String updateSQL = "UPDATE `participants` SET `fingerprint` = ? WHERE `event_id` = ?";

        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(updateSQL);

            pstmt.setBytes(1, data);
            pstmt.setString(2, event_id);
            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't connect to database",
                    "Error to connect...", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return false;

    }

}
