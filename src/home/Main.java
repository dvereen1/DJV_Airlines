package home;

import home.DB_Connection.DBConnect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        DBConnect.connect();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/SplashPage.fxml"));
        primaryStage.setTitle("S.E.G Airlines");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
