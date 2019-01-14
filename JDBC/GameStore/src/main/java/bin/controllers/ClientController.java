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


public class ClientController {

    Logging logging = new Logging();
    private ObservableList<Game> gameData = FXCollections.observableArrayList();

    @FXML
    ListView<String> myGameList;
    @FXML
    Tab tabName;
    @FXML
    Label libraryGameLabel;
    @FXML
    Button playButton;
    @FXML
    Label ratingLabel;
    @FXML
    TextField rateTextField;
    @FXML
    Button rateButton;
    @FXML
    TextField searchTextField;
    @FXML
    TableView<Game> tableView;
    @FXML
    TableColumn<Game, String> nameColumn;
    @FXML
    TableColumn<Game, String> devColumn;
    @FXML
    TableColumn<Game, String> ratingColumn;
    @FXML
    TableColumn<Game,String> priceColumn;
    @FXML
    ChoiceBox<String> ratingChoiceBox;
    @FXML
    TextField storeSearchTextField;
    @FXML
    ChoiceBox<String> priceCB;
    @FXML
    CheckBox indieCB;
    @FXML
    CheckBox multiplayerCB;
    @FXML
    CheckBox strategyCB;
    @FXML
    CheckBox competitiveCB;
    @FXML
    CheckBox tacticalCB;
    @FXML
    CheckBox openWorldCB;
    @FXML
    CheckBox rpgCB;
    @FXML
    CheckBox shooterCB;
    @FXML
    CheckBox casualCB;
    @FXML
    TextArea textArea;
    @FXML
    Label gameLabel;
    @FXML
    Label buyInfoLabel;
    @FXML
    Label nameLabel;
    @FXML
    ChoiceBox<String> countryChoiceBox;
    @FXML
    Label balanceLabel;
    @FXML
    ChoiceBox<String> foundsChoiceBox;
    @FXML
    TextField cardTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    Label infoLabel;

    @FXML
    public void initialize() throws SQLException {
        tabName.setText(Main.login);
        nameLabel.setText(Main.login);
        fillOwnGameList("");
        fillCountryChoiceBox();
        libraryGameLabel.setVisible(false);
        ratingLabel.setVisible(false);
        playButton.setVisible(false);
        rateButton.setVisible(false);
        rateTextField.setVisible(false);
        buyInfoLabel.setVisible(false);
        infoLabel.setVisible(false);
        fillRatingCB();
        fillPriceCB();
        fillFoundsChoiceBox();
        fillTableView("");
        tabInit();
        balanceLabel.setText(Integer.toString(getMoney()));
    }

    private void fillFoundsChoiceBox() {
        foundsChoiceBox.getItems().add("60");
        foundsChoiceBox.getItems().add("100");
        foundsChoiceBox.getItems().add("200");
        foundsChoiceBox.getItems().add("300");
        foundsChoiceBox.getItems().add("500");
        foundsChoiceBox.getItems().add("1000");
        foundsChoiceBox.setValue("300");
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
        countryChoiceBox.setValue("USA");
    }

    private void fillPriceCB() {
        priceCB.getItems().add("10");
        priceCB.getItems().add("25");
        priceCB.getItems().add("50");
        priceCB.getItems().add("100");
        priceCB.getItems().add("150");
        priceCB.getItems().add("200");
        priceCB.getItems().add("any");
        priceCB.setValue("any");
    }

    private void fillRatingCB() {
        for (int i = 1; i < 11; i++) {
            ratingChoiceBox.getItems().add("" + i + "");
        }
        ratingChoiceBox.setValue("1");
    }

    private void fillOwnGameList(String search) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT name FROM history INNER JOIN games g on history.game = g.id WHERE customer = " + Main.id;
        if (!search.equals("")) query += " AND name LIKE \"%" + search + "%\"";
        ResultSet resultSet = statement.executeQuery(query);
        String result ;
        while (resultSet.next()){
            result = resultSet.getString(1);
            myGameList.getItems().add(result);
        }
        resultSet.close();
        statement.close();

    }

    private void tabInit() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));
        devColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("dev"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("rate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("price"));
    }

    private void fillTableView(String search) throws SQLException {
        tableView.getItems().clear();
        gameData.clear();

        String query = "SELECT games.name, d.name, rating, price FROM games " +
                "INNER JOIN developer d on games.developer = d.id";
        query += " WHERE rating >= " + ratingChoiceBox.getValue();
        if (!priceCB.getValue().equals("any")) {
            query += " AND price <= " + priceCB.getValue();
        }
        if (!search.equals("")) {
            query += " AND games.name LIKE \"%" + search + "%\"";
        }
        if (indieCB.isSelected()) {
            query += " AND games.tags LIKE \"%indie%\"";
        }
        if (multiplayerCB.isSelected()) {
            query += " AND games.tags LIKE \"%myltiplayer%\"";
        }
        if (strategyCB.isSelected()) {
            query += " AND games.tags LIKE \"%strategy%\"";
        }
        if (competitiveCB.isSelected()) {
            query += " AND games.tags LIKE \"%competitive%\"";
        }
        if (tacticalCB.isSelected()) {
            query += " AND games.tags LIKE \"%tactical%\"";
        }
        if (openWorldCB.isSelected()) {
            query += " AND games.tags LIKE \"%open%\"";
        }
        if (rpgCB.isSelected()) {
            query += " AND games.tags LIKE \"%rpg%\"";
        }
        if (shooterCB.isSelected()) {
            query += " AND games.tags LIKE \"%shooter%\"";
        }
        if (casualCB.isSelected()) {
            query += " AND games.tags LIKE \"%casual%\"";
        }

        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            gameData.add(new Game(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4)));
        }
        resultSet.close();
        statement.close();

        tableView.setItems(gameData);
    }

    public void storeGameSelected() throws SQLException {
        textArea.setText("");
        gameLabel.setText(tableView.getSelectionModel().getSelectedItem().getName());
        int game = getGameID(gameLabel.getText());
        String text = "Developer: " + tableView.getSelectionModel().getSelectedItem().getDev() + "\n\n";
        text += "Rating: " + tableView.getSelectionModel().getSelectedItem().getRate() + "\n\n";
        text += getinfo(game) + "\n\n";
        text += getSysReq(game) + "\n\n";
        text += "Price: " +  tableView.getSelectionModel().getSelectedItem().getPrice() + "\n\n";

        textArea.appendText(text);
    }

    private String getinfo(int game) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT tags, languages FROM games WHERE id = " + game;
        ResultSet resultSet = statement.executeQuery(query);
        String result = "" ;
        while (resultSet.next()){
            result = "tags: " + resultSet.getString(1) + "\n\n";
            result += "languages: " + resultSet.getString(2) + "\n\n";
        }
        resultSet.close();
        statement.close();

        return result;
    }

    private String getSysReq(int game) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT os, processor, graphics, directX, storage FROM games INNER JOIN SystemRequirements SR " +
                "on games.sysReq = SR.ID WHERE games.id =" + game;
        ResultSet resultSet = statement.executeQuery(query);
        String result = "System requirements :\n" ;
        while (resultSet.next()){
            result += "OS: " + resultSet.getString(1) + "\n";
            result += "processor: " + resultSet.getString(2) + "\n";
            result += "graphics: " + resultSet.getString(3) + "\n";
            result += "directX: " + resultSet.getString(4) + "\n";
            result += "storage: " + resultSet.getString(5) + "\n";
        }
        resultSet.close();
        statement.close();

        return result;
    }

    public void libraryGameSelected() throws SQLException {
        libraryGameLabel.setText(myGameList.getSelectionModel().getSelectedItem());
        libraryGameLabel.setVisible(true);
        playButton.setVisible(true);
        rateGame();
    }

    public void playButtonPressed() throws SQLException {
        Main.window.toBack();
        logging.addLog("playing " + libraryGameLabel.getText());
    }

    public void librarySearchButtonPressed() throws SQLException {
        myGameList.getItems().clear();
        fillOwnGameList(searchTextField.getText());
    }

    public void storeSearchButtonPressed() throws SQLException {
        tableView.getItems().clear();
        gameData.clear();
        fillTableView(storeSearchTextField.getText());
    }

    public void buyButtonPressed() throws SQLException {
        int money = getMoney();
        if (gameLabel.getText().equals("Game")) {
            return;
        }
        int game = getGameID(gameLabel.getText());
        int price = getPrice(game);
        LabelGame lb = new LabelGame();

        if (checkIfBought(game)) {
            lb.visibleLable(buyInfoLabel, "olready", 2);
            return;
        }
        if (money >= price) {
            lb.visibleLable(buyInfoLabel, "OK", 2);
            makeBuy(game);
            setMoney(money-price);
        } else {
            lb.visibleLable(buyInfoLabel, "low balance", 2);
        }
    }

    private void setMoney(int m) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        String insertUser = "UPDATE users SET money = " + m + " WHERE id = " + Main.id;
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(insertUser);

        ps.executeUpdate();
        ps.close();
        balanceLabel.setText(Integer.toString(getMoney()));
    }

    private void makeBuy(int g) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        String insertUser = "INSERT INTO history VALUES (?, ?, now())";
        PreparedStatement ps = dbWorker.getConnection().prepareStatement(insertUser);
        ps.setString(1, Integer.toString(g));
        ps.setString(2, Integer.toString(Main.id));

        ps.executeUpdate();
        ps.close();

        Logging logging = new Logging();
        logging.addLog("buoght game: " + g);
    }

    private boolean checkIfBought(int g) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT * FROM history WHERE game = " + g + " AND customer = " + Main.id;
        ResultSet resultSet = statement.executeQuery(query);
        String result = "";
        while (resultSet.next()){
            result = resultSet.getString(3);
        }
        resultSet.close();
        statement.close();

        if (result.equals("")) return false;
        return true;
    }

    private int getPrice(int g) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT price FROM games WHERE id = " + g;
        ResultSet resultSet = statement.executeQuery(query);
        int result = 0;
        while (resultSet.next()){
            result = resultSet.getInt(1);
        }
        resultSet.close();
        statement.close();

        return result;
    }

    private int getMoney() throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT money FROM users WHERE id = " + Main.id;
        ResultSet resultSet = statement.executeQuery(query);
        int result = 0;
        while (resultSet.next()){
            result = resultSet.getInt(1);
        }
        resultSet.close();
        statement.close();

        return result;
    }

    public void changeButtonPressed() throws SQLException {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        int country = getCounrtyNumber(countryChoiceBox.getValue());

        DBWorker dbWorker = new DBWorker();
        String updateUser = "UPDATE users SET country = " + country;
        if (!name.equals("")) {
            updateUser += ", name = \"" + name + "\"";
        }
        if (!email.equals("")) {
            updateUser += ", email = \"" + email + "\"";
        }
        updateUser += "WHERE id = " + Main.id;

        PreparedStatement ps = dbWorker.getConnection().prepareStatement(updateUser);

        ps.executeUpdate();
        ps.close();
        balanceLabel.setText(Integer.toString(getMoney()));
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

    public void addButtonPressed() throws SQLException {
        LabelGame lg = new LabelGame();
        if (!checkCard()) {
            lg.visibleLable(infoLabel, "wrong card", 2);
            return;
        }
        setMoney(getMoney() + Integer.parseInt(foundsChoiceBox.getValue()));
        lg.visibleLable(infoLabel, "DONE", 2);
        Logging logging = new Logging();
        logging.addLog("add founds: " + foundsChoiceBox.getValue());
    }

    private boolean checkCard() {
        String card = cardTextField.getText();
        if ((!card.matches("^\\d+$"))  || (card.length() != 16)) {
            return false;
        }
        return true;
    }

    public void rateButtonPressed() throws SQLException {
        String rating = rateTextField.getText();
        for (int i = 1; i < 11; i++) {
            if (rating.equals(Integer.toString(i))) {
                rateTextField.setVisible(false);
                rateButton.setVisible(false);
                ratingLabel.setVisible(true);
                ratingLabel.setText("Rated: " + Integer.toString(i));

                String game = Integer.toString(getGameID(myGameList.getSelectionModel().getSelectedItem()));
                if (game.equals("0")) return;

                DBWorker dbWorker = new DBWorker();
                String rate = "INSERT INTO ratings VALUES (?, ?, ?, now())";
                PreparedStatement ps = dbWorker.getConnection().prepareStatement(rate);
                ps.setString(1, game);
                ps.setString(2, Integer.toString(Main.id));
                ps.setString(3, rateTextField.getText());

                ps.executeUpdate();
                ps.close();
                rateTextField.setText("");

                Logging logging = new Logging();
                logging.addLog("Game " + game + " reated on " + i);
            }
        }
    }

    private void rateGame() throws SQLException {
        rateTextField.setVisible(false);
        rateButton.setVisible(false);
        ratingLabel.setVisible(false);

        int gID = getGameID(libraryGameLabel.getText());
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT rating FROM ratings WHERE customer = " + Main.id + " AND game = " + gID;
        ResultSet resultSet = statement.executeQuery(query);
        int result = 0;
        while (resultSet.next()){
            result = Integer.parseInt(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        if (result == 0) {
            rateTextField.setVisible(true);
            rateButton.setVisible(true);
        } else {
            ratingLabel.setVisible(true);
            ratingLabel.setText("Rated: " + result);
        }
    }

    private int getGameID(String name) throws SQLException {
        DBWorker dbWorker = new DBWorker();
        Statement statement = dbWorker.getConnection().createStatement();
        String query = "SELECT id FROM games WHERE name = \"" + name + "\"";
        ResultSet resultSet = statement.executeQuery(query);
        int result =0 ;
        while (resultSet.next()){
            result = Integer.parseInt(resultSet.getString(1));

        }
        resultSet.close();
        statement.close();

        return result;
    }

}
