package home.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import home.DB_Connection.DBConnect;
import home.DB_Models.LoginModel;
import home.DB_Models.Passengers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    LoginModel loginModel = new LoginModel();

    @FXML
    private Label dbStatus;
    @FXML
    private Label loginStatus;

    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private ComboBox<Option> combobox;
    @FXML
    private Button loginBtn;

    private String fName;
    private String lName;
    private String username;






    //ObservableList<String> list = FXCollections.observableArrayList("Admin", "Passenger");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Checking if database connected and
            if(this.loginModel.isDatabaseConnected()){
                this.dbStatus.setText("Connected to Database");
            }else{
                this.dbStatus.setText("Disconnected from Database");
            }
            //setting the options within ComboBox to the values in the enum class
             combobox.setItems(FXCollections.observableArrayList(Option.values()));
    }

    @FXML
    public void login(ActionEvent event){
        try{
            //now we are checking if the items in the textFields and passwords fields check out in the database
            if(this.loginModel.isLogin(this.userName.getText(), this.password.getText(), (this.combobox.getValue()).toString())){
                //we are closing the stage which contains the login button
               Stage stage = (Stage)this.loginBtn.getScene().getWindow();
               stage.close();

               switch((this.combobox.getValue()).toString()){
                   case "Admin":
                       adminLogin();
                       break;
                   case "Passenger":
                       passengerLogin(event);
                       break;

               }
               this.loginStatus.setText("LOGGING IN TEST");
            }else{
                this.loginStatus.setText("Incorrect Credentials");
            }

        }catch(Exception localException){

        }
    }


    public void passengerLogin(ActionEvent actionEvent) throws IOException{

                try{

                    Connection conn = DBConnect.getConnection();

;                   String getUserInfo =  "Select fName, lName, userName FROM passenger WHERE userName = ?";
                    PreparedStatement stmt = conn.prepareStatement(getUserInfo);

                    stmt.setString(1, userName.getText());


                    ResultSet resultSet = stmt.executeQuery();


                    while(resultSet.next()){
                         /*currentPassenger = new Passengers(resultSet.getString(1), resultSet.getString(2),
                            resultSet.getString(3),resultSet.getString(4),
                            resultSet.getString(5), resultSet.getString(6));*/

                        fName = resultSet.getString(1);
                        lName =  resultSet.getString(2);
                        username = resultSet.getString(3);

                    }
                   System.out.println("this is the first name: " + fName);






                    conn.close();

                }catch(SQLException err){
                    System.err.println("Error: " + err);
                }

                Stage passengerStage = new Stage();

                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("../fxml/Main.fxml").openStream());
                //Parent root = loader.load(getClass().getResource("../fxml/Main.fxml"));
                //Stage passengerStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                passengerStage.setScene(scene);

                Controller control = loader.getController();
                //System.out.println(currentPassenger.getfName());
                /*System.out.println("this is the first name: " + fName);
                System.out.println("this is the last name: " + lName);
                System.out.println("this is the ticketid: " + ticketId);*/


                control.getPassengerInfo(username, fName, lName);

               // passengerStage.setResizable(false);
                passengerStage.show();
            }



    public void adminLogin(){
        try{
            Stage adminStage = new Stage();
            FXMLLoader adminLoader = new FXMLLoader();
            Pane root = adminLoader.load(getClass().getResource("../fxml/Admin.fxml").openStream());

            Scene scene = new Scene(root);
            adminStage.setScene(scene);
            adminStage.setResizable(false);
            adminStage.show();
        }catch (IOException ex){
            ex.printStackTrace();;
        }

    }

    /*public boolean isValidCredentials(){
        boolean let_in =
    }*/
}
