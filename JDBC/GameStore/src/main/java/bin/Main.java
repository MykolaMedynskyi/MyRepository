package bin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


public class Main extends Application {

    public static Stage window;
    public static Scene loginS;

    public static String login;
    public static int id;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        URL url = new File("src/main/resources/fxml/login.fxml").toURL();
        Parent root = FXMLLoader.load(url);
        window.setTitle("Games4u");
        loginS = new Scene(root);
        window.setScene(loginS);
        window.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
