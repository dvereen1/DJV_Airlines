package home.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SplashPage {

    @FXML
    public void goMain(ActionEvent actionEvent) throws IOException{
        Parent homePage = FXMLLoader.load(getClass().getResource("../fxml/Main.fxml"));
        Scene home = new Scene(homePage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(home);
        window.show();
        System.out.println("Begin Journey Button Clicked");

    }


    @FXML
    public void goLogin(ActionEvent actionEvent) throws IOException{
        Parent loginPage = FXMLLoader.load(getClass().getResource("../fxml/Login.fxml"));
        Scene login = new Scene(loginPage);

        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.close();

        window.setScene(login);
        window.show();
        System.out.println("Login Clicked");

    }
}
