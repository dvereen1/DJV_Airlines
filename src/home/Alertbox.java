package home;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alertbox {

        public static void display(String title, String message){
            Stage window  = new Stage();

            //The following line prevents interaction with any things else but the window that has focus.
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            window.setMinWidth(250);

            Label label = new Label();
            label.setText(message);
            Button closeButton = new Button("Close the window");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox();
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();


        }
}

