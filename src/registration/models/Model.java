package registration.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void insertParticipant(String lastName, String firstName, String middleName, String contactNumber,
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // System.out.println(sql);
    }

}
