package bin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception{
        Scene application = new Scene(FXMLLoader.load(getClass().getResource("fx/Application.fxml")), 400, 500);
        window.setTitle("Let's tessellate");
        window.setScene(application);
        window.getIcons().add(new Image("bin/images/icon.png"));
        window.setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-window.getScene().getWidth()/2);
        window.setY(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-window.getScene().getHeight()/2);
        window.setResizable(false);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
