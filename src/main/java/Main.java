package main.java;

import main.java.home.controllers.SplashPage;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.home.controllers.UserRegistrationController;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //DBConnect.connect();

        FXMLLoader loadURC = new FXMLLoader(getClass().getResource("home/fxml/UserRegistration.fxml"));
        loadURC.load();
        UserRegistrationController urc = loadURC.getController();
        urc.setYearArray();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("home/fxml/SplashPage.fxml"));
        Parent root = loader.load();

        SplashPage sPC = loader.getController();
        sPC.updateFlights();

        primaryStage.setTitle("DJV Airlines");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
