package registration.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import registration.models.Model;

import javax.swing.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {

    @FXML
    private TextField lastName, firstName, middleName, contactNumber, studentNumber, section;

    @FXML
    private ToggleGroup gender, payment_method;

    private RadioButton radio_gender, radio_payment;

    private String strLastName, strFirstName, strMiddleName, strContactNumber, strGender, strStudentNumber,
            strSection, strPaymentMethod;

    private final static Pattern strPattern = Pattern.compile("[a-zA-Z]+");
    private final static Pattern contactPattern = Pattern.compile("^[0-9,-]+$");

    @FXML
    void submitInfo(ActionEvent event) throws IOException {

        strLastName = lastName.getText();
        strFirstName = firstName.getText();
        strMiddleName = middleName.getText();
        strContactNumber = contactNumber.getText();

        radio_gender = (RadioButton) gender.getSelectedToggle();
        strGender = radio_gender.getText();

        strStudentNumber = studentNumber.getText();
        strSection = section.getText();

        radio_payment = (RadioButton) payment_method.getSelectedToggle();
        strPaymentMethod = radio_payment.getText();

        try {
            //validate();
            //putDataInDatabase();
            goToNextScene();
        } catch (Exception e) {
            String prompt = e.toString().substring(21);
            JOptionPane.showMessageDialog(null, prompt, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void validate() throws Exception {

        Matcher match;

        match = strPattern.matcher(strLastName);
        matcher(strLastName, "Last Name", match);

        match = strPattern.matcher(strFirstName);
        matcher(strFirstName, "First Name", match);

        match = strPattern.matcher(strMiddleName);
        matcher(strMiddleName, "Middle Name", match);

        match = contactPattern.matcher(strContactNumber);
        matcher(strContactNumber, "Contact Number", match);

        nullPointer(strStudentNumber, "Student Number");

        nullPointer(strSection, "Section");

    }

    //For null input
    private void nullPointer(String test, String type) throws Exception {
        if (test.equals("")) {
            throw new Exception("Enter your " + type);
        }
    }

    //For RegEx Validation
    private void matcher(String test, String type, Matcher match) throws Exception {
        nullPointer(test, type);
        if (!match.find()) {
            throw new Exception("Invalid " + type);
        }
    }

    private void goToNextScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/xml/fingerprint.fxml"));
        loader.setController(new Fingerprint());

        Parent root = loader.load();
        Stage stage = (Stage) lastName.getScene().getWindow();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();

    }

    private void putDataInDatabase() {
        Model model = Model.getInstance();

        model.insertParticipant(strLastName, strFirstName, strMiddleName, strContactNumber, strGender,
                strStudentNumber, strSection, strPaymentMethod);

    }

}
