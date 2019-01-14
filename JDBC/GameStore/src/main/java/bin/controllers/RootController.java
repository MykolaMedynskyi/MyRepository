package bin.controllers;

import bin.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RootController {

    private Logging logging = new Logging();
    private LabelGame lg = new LabelGame();
    private ObservableList<Log> logData = FXCollections.observableArrayList();
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Game> gameData = FXCollections.observableArrayList();
    private ObservableList<Developer> devData = FXCollections.observableArrayList();



    @FXML
    TableView<Log> logTable;
    @FXML
    TableColumn<Log, String> userColumn;
    @FXML
    TableColumn<Log, String> actionColumn;
    @FXML
    TableColumn<Log, String> dateColumn;
    @FXML
    TableView<User> userTable;
    @FXML
    TableColumn<User, String> idColumn;
    @FXML
    TableColumn<User, String> loginColumn;
    @FXML
    TextField searchTextField;
    @FXML
    Label deleteLabel;
    @FXML
    TableView<Game> gameTable;
    @FXML
    TableColumn<Game, String> idGameColumn;
    @FXML
    TableColumn<Game, String> nameGameColumn;
    @FXML
    TextField searchGameTextField;
    @FXML
    Label deleteGameLabel;
    @FXML
    TableView<Developer> devTable;
    @FXML
    TableColumn<Developer, String> idDevColumn;
    @FXML
    TableColumn<Developer, String> nameDevColumn;
    @FXML
    TextField searchDevTextField;
    @FXML
    Label deleteDevLabel;
    @FXML
    Label addUserLabel;
    @FXML
    TextField loginTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    ChoiceBox<String> countryCB;
    @FXML
    ChoiceBox<String> accessCB;
    @FXML
    TextField backupNameTextField;
    @FXML
    Label backupLabel;
    @FXML
    TextField restoreNameTextField;


    @FXML
    public void initialize() throws SQLException {
        tabInit();
        labelInit();
        fillLogTable();
        fillUserTable("");
        fillGameTable("");
        fillDevTable("");
        initCountryCB();
        initAccessCB();
    }

    private void initCountryCB() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT country FROM country";
        ResultSet resultSet = statement.executeQuery(query);
        String result = " ";
        while (resultSet.next()){
            result = resultSet.getString(1);
            countryCB.getItems().add(result);
        }
        resultSet.close();
        statement.close();
        countryCB.setValue("USA");
    }

    private void initAccessCB() {
        accessCB.getItems().add("root");
        accessCB.getItems().add("admin");
        accessCB.setValue("admin");
    }

    private void labelInit() {
        deleteLabel.setVisible(false);
        deleteGameLabel.setVisible(false);
        deleteDevLabel.setVisible(false);
        addUserLabel.setVisible(false);
        backupLabel.setVisible(false);
    }

    private void tabInit() {
        userColumn.setCellValueFactory(new PropertyValueFactory<Log, String>("user"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<Log, String>("action"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Log, String>("date"));

        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        idGameColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("id"));
        nameGameColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));

        idDevColumn.setCellValueFactory(new PropertyValueFactory<Developer, String>("id"));
        nameDevColumn.setCellValueFactory(new PropertyValueFactory<Developer, String>("name"));
    }

    private void fillLogTable() throws SQLException {
        logData.clear();
        logTable.getItems().clear();

        String query = "SELECT user, action, date FROM logs";
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            logData.add(new Log(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
        }
        resultSet.close();
        statement.close();

        logTable.setItems(logData);
    }

    private void fillUserTable(String search) throws SQLException {
        userData.clear();
        userTable.getItems().clear();

        String query = "SELECT id, login FROM users WHERE id <> " + Main.id;
        if (!search.equals("")) {
            query += " AND login LIKE \"%" + search + "%\"";
        }
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            userData.add(new User(resultSet.getString(1), resultSet.getString(2)));
        }
        resultSet.close();
        statement.close();

        userTable.setItems(userData);
    }

    private void fillGameTable(String search) throws SQLException {
        gameData.clear();
        gameTable.getItems().clear();

        String query = "SELECT id, name FROM games";
        if (!search.equals("")) {
            query += " WHERE name LIKE \"%" + search + "%\"";
        }
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            gameData.add(new Game(resultSet.getString(1), resultSet.getString(2)));
        }
        resultSet.close();
        statement.close();

        gameTable.setItems(gameData);
    }

    public void reloadButtonPressed() throws SQLException {
        fillLogTable();
    }

    public void searchButtonPressed() throws SQLException {
        fillUserTable(searchTextField.getText());
    }

    public void deleteButtonPressed() throws SQLException {
        int id = Integer.parseInt(userTable.getSelectionModel().getSelectedItem().getId());

        DBWorker dbWorker = new DBWorker();
        String ask = "DELETE FROM users WHERE id = " + id;
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(ask);

        ps.executeUpdate();
        ps.close();
        logging.addLog("deleted user: " + id);
        lg.visibleLable(deleteLabel, "deleted", 2);
        fillUserTable(searchTextField.getText());
    }

    public void searchGameButtonPressed() throws SQLException {
        fillGameTable(searchGameTextField.getText());
    }

    private void fillDevTable(String search) throws SQLException {
        devData.clear();
        devTable.getItems().clear();

        String query = "SELECT id, name FROM developer";
        if (!search.equals("")) {
            query += " WHERE name LIKE \"%" + search + "%\"";
        }
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            devData.add(new Developer(resultSet.getString(1), resultSet.getString(2)));
        }
        resultSet.close();
        statement.close();

        devTable.setItems(devData);
    }

    public void deleteGameButtonPressed() throws SQLException {
        int id = Integer.parseInt(gameTable.getSelectionModel().getSelectedItem().getId());

        DBWorker dbWorker = new DBWorker();
        String ask = "DELETE FROM games WHERE id = " + id;
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(ask);

        ps.executeUpdate();
        ps.close();
        logging.addLog("deleted game: " + id);
        lg.visibleLable(deleteGameLabel, "deleted", 2);
        fillGameTable(searchGameTextField.getText());
    }

    public void deleteDevButtonPressed() throws SQLException {
        int id = Integer.parseInt(devTable.getSelectionModel().getSelectedItem().getId());

        DBWorker dbWorker = new DBWorker();
        String ask = "DELETE FROM developer WHERE id = " + id;
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(ask);

        ps.executeUpdate();
        ps.close();
        logging.addLog("deleted developer: " + id);
        lg.visibleLable(deleteDevLabel, "deleted", 2);
        fillDevTable(searchDevTextField.getText());
        fillGameTable(searchGameTextField.getText());
    }

    public void searchDevButtonPressed() throws SQLException {
        fillDevTable(searchDevTextField.getText());
    }

    public void addUserButtonPressed() throws SQLException {
        String login = loginTextField.getText();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        int country = getCounrtyNumber(countryCB.getValue());
        String access = accessCB.getValue();
        String password = Integer.toString(login.hashCode());

        if (login.equals("") || name.equals("") || email.equals("")) {
            lg.visibleLable(addUserLabel, "insert data...", 2);
            return;
        }

        DBWorker dbWorker = new DBWorker();
        String insertUser = "INSERT INTO users (login, name, email, country, password, access)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(insertUser);
        ps.setString(1, login);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setInt(4, country);
        ps.setString(5, password);
        ps.setString(6, access);

        lg.visibleLable(addUserLabel, "Done", 2);
        ps.executeUpdate();
        ps.close();
        logging.addLog("Add new user: " + name);
        loginTextField.setText("");
        emailTextField.setText("");
        nameTextField.setText("");
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

    public void backopButtonPressed() throws Exception {
        String name = backupNameTextField.getText();

        String dump = " mysqldump -u root -p123456 GameStore > /home/mykola/mykola/s3/BD/lista4/backups/" + name + ".sql";
        String[] cmdarray = {"/bin/sh","-c", dump};
        Process p = Runtime.getRuntime().exec(cmdarray);
        if (p.waitFor() == 0) {
            lg.visibleLable(backupLabel, "Done", 2);
        } else {
            lg.visibleLable(backupLabel, "Error", 2);
        }
        backupNameTextField.setText("");
    }

    public void restoreButtonPressed() throws Exception {
        String name = restoreNameTextField.getText();

        String restore = "mysql -u root -p123456 GameStore < /home/mykola/mykola/s3/BD/lista4/backups/" + name + ".sql";
        String[] cmdarray = {"/bin/sh","-c", restore};
        Process p = Runtime.getRuntime().exec(cmdarray);
        if (p.waitFor() == 0) {
            lg.visibleLable(backupLabel, "Done", 2);
        } else {
            lg.visibleLable(backupLabel, "Error", 2);
        }
        restoreNameTextField.setText("");
    }

}
