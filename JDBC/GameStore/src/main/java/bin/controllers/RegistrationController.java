package bin.controllers;

import bin.DBWorker;
import bin.LabelGame;
import bin.Logging;
import bin.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static bin.Main.loginS;

public class RegistrationController {

    @FXML
    Label infoLabel;
    @FXML
    TextField loginTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    RadioButton mRB;
    @FXML
    DatePicker bdDatePicker;
    @FXML
    PasswordField passwordField;
    @FXML
    ChoiceBox<String> countryChoiceBox;

    @FXML
    public void initialize() throws SQLException {
        mRB.fire();
        fillCountryChoiceBox();
        countryChoiceBox.setValue("USA");
        infoLabel.setVisible(false);
    }

    private void fillCountryChoiceBox() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT country FROM country";
        ResultSet resultSet = statement.executeQuery(query);
        String result = " ";
        while (resultSet.next()){
            result = resultSet.getString(1);
            countryChoiceBox.getItems().add(result);
        }
        resultSet.close();
        statement.close();
    }

    public void backButtonPressed() {
        Main.window.setScene(loginS);
    }

    public void registerButtonPressed() throws SQLException {
        String login = loginTextField.getText();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        int country = getCounrtyNumber(countryChoiceBox.getValue());
        String password = Integer.toString(passwordField.getText().hashCode());

        LabelGame lg = new LabelGame();

        if (login.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() ||
        phoneTextField.getText().isEmpty()) {
            lg.visibleLable(infoLabel, "enter the data", 2);
            return;
        }
        if (!checkLogin(login)) {
            lg.visibleLable(infoLabel, "login already exists", 2);
            return;
        }

        DBWorker dbWorker = new DBWorker();
        String insertUser = "INSERT INTO users (login, name, email, country, password, access, money)" +
                " VALUES (?, ?, ?, ?, ?, 'customer', 0)";
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(insertUser);
        ps.setString(1, login);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setInt(4, country);
        ps.setString(5, password);

        lg.visibleLable(infoLabel, "Done", 2);
        ps.executeUpdate();
        ps.close();
    }

    private int getCounrtyNumber(String c) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT id FROM country WHERE country = \"" + c + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        int result = 0;
        while (resultSet.next()){
            result = Integer.parseInt(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        return result;
    }

    private boolean checkLogin(String login) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT id FROM users WHERE login = \"" + login + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        String result = "";
        while (resultSet.next()){
            result = resultSet.getString(1);
        }
        resultSet.close();
        statement.close();
        System.out.println(result + " sssssssssssssssssssssssssssssss");
        if (result.equals("")) return true;
        return false;
    }

}
