package bin.controllers;

import bin.DBWorker;
import bin.LabelGame;
import bin.Logging;
import bin.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static bin.Main.loginS;

public class LoginController {

    private static Scene registrationScene;
    private static Scene rootScene;
    private static Scene adminScene;
    private static Scene clientScene;

    @FXML
    TextField loginTextField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label infoLabel;

    @FXML
    public void initialize() throws IOException {
        createRegistrationScene();
        infoLabel.setVisible(false);
    }

    private void createRegistrationScene() throws IOException {
        URL url = new File("src/main/resources/fxml/registration.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        registrationScene = new Scene(root);
    }

    private void createRootScene() throws IOException {
        URL url = new File("src/main/resources/fxml/root.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        rootScene = new Scene(root);
    }

    private void createAdminScene() throws IOException {
        URL url = new File("src/main/resources/fxml/admin.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        adminScene = new Scene(root);
    }

    private void createClientScene() throws IOException {
        URL url = new File("src/main/resources/fxml/client.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        clientScene = new Scene(root);
    }

    public void exitButtonPressed() {
        Main.window.close();
    }

    public void registrationButtonPressed() {
        Main.window.setScene(registrationScene);

        registrationScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Main.window.setScene(loginS);
            }
            event.consume();
        });
    }

    public void loginButtonPressed() throws Exception {
        String login = loginTextField.getText();
        String password = passwordField.getText();
        LabelGame lg = new LabelGame();

        if (login.isEmpty() || password.isEmpty()) {
            lg.visibleLable(infoLabel, "enter login and pass", 2);
            return;
        }
        int id = whatID(login, password);
        if (id == 0) {
            lg.visibleLable(infoLabel, "wrong login or pass", 2);
            return;
        }

        Main.login = login;
        Main.id = id;
        String access = getAccess(id);


        if (access.equals("customer")) {
            createClientScene();
            Main.window.setScene(clientScene);
        }
        if (access.equals("admin")) {
            createAdminScene();
            Main.window.setScene(adminScene);
        }
        if (access.equals("root")) {
            createRootScene();
            Main.window.setScene(rootScene);
        }

        Logging logging = new Logging();
        logging.addLog("logged in");

    }

    private int whatID(String login, String password) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT id FROM users WHERE login = \"" + login + "\" AND password = \"" + password.hashCode() + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        int result = 0;
        while (resultSet.next()){
            result = Integer.parseInt(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();

        return result;
    }

    private String getAccess(int id) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT access FROM users WHERE id = \"" + id + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        String result = "";
        while (resultSet.next()){
            result = resultSet.getString(1);
        }
        resultSet.close();
        statement.close();

        return result;
    }

}
