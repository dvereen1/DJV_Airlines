package main.java.home.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import main.java.home.DB_Connection.DBConnect;
import main.java.home.DB_Models.LoginModel;
//import main.java.home.DB_Models.Passengers;
import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
//import java.util.Scanner;

public class LoginController implements Initializable {

    private LoginModel loginModel = new LoginModel();

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
private int timeout = 0;





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
                       passengerLogin();
                       break;

               }
               this.loginStatus.setText("LOGGING IN TEST");
            }else{

                this.loginStatus.setText("Incorrect Credentials");
                loginStatus.setTextFill(Color.RED);

                userName.setText("");
                password.setText("");
                combobox.valueProperty().set(null);
                System.out.println("Tries until lockout" + timeout);
                timeout++;
                System.out.println("Tries until lockout" + timeout);
                if(timeout == 3){


                    //FXMLLoader loader = new FXMLLoader();
                    Pane root = FXMLLoader.load(getClass().getResource("../fxml/SplashPage.fxml"));
                    //Parent root = loader.load(getClass().getResource("../fxml/Main.fxml"));
                    Stage splashStage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    splashStage.setScene(scene);

                    //Controller control = loader.getController();
                    //System.out.println(currentPassenger.getfName());
                /*System.out.println("this is the first name: " + fName);
                System.out.println("this is the last name: " + lName);
                System.out.println("this is the ticketid: " + ticketId);*/




                    // passengerStage.setResizable(false);
                    splashStage.show();
                }
            }

        }catch(Exception localException){
            localException.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent --
     * @throws IOException
     * Going to the register scene if the user decides to register.
     */

    @FXML
    public void signup(ActionEvent actionEvent) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/UserRegistration.fxml"));
        Parent root =  loader.load();
        UserRegistrationController regController = loader.getController();
        regController.setTicketConfirm(true);

        Scene registerScene = new Scene(root);
        Stage regStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        regStage.setScene(registerScene);
        regStage.show();

    }



    private void passengerLogin(/*ActionEvent actionEvent*/) throws IOException{

                try{

                    Connection conn = DBConnect.getConnection();

                 String getUserInfo =  "Select fName, lName, userName FROM passenger WHERE userName = ?";
                    PreparedStatement stmt = conn != null ? conn.prepareStatement(getUserInfo) : null;

                    if (stmt != null) {
                        stmt.setString(1, userName.getText());
                    }


                    ResultSet resultSet = stmt != null ? stmt.executeQuery() : null;


                    while(resultSet != null && resultSet.next()){
                         /*currentPassenger = new Passengers(resultSet.getString(1), resultSet.getString(2),
                            resultSet.getString(3),resultSet.getString(4),
                            resultSet.getString(5), resultSet.getString(6));*/

                        fName = resultSet.getString(1);
                        lName =  resultSet.getString(2);
                        username = resultSet.getString(3);

                    }
                   System.out.println("this is the first name: " + fName);


                    assert conn != null;
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



    private void adminLogin(){
       /* Scanner scan = new Scanner(System.in);

        System.out.println("Enter a password for admin: ");
        String pass = scan.nextLine();

        try{
            String sqlStr = "UPDATE login SET password = " + pass + " WHERE userName = admin1";
            Connection conn = DBConnect.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(sqlStr);


        }catch(SQLException err){
            System.out.println("Error: " + err);

        }*/

        try{
            Stage adminStage = new Stage();
            FXMLLoader adminLoader = new FXMLLoader();
            Pane root = adminLoader.load(getClass().getResource("../fxml/Admin.fxml").openStream());

            Scene scene = new Scene(root);
            adminStage.setScene(scene);
            adminStage.setResizable(false);
            adminStage.show();

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /*public boolean isValidCredentials(){
        boolean let_in =
    }*/
}
