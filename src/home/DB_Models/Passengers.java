package home.DB_Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Passengers {

    private final StringProperty fName;
    private final StringProperty lName;
    private final StringProperty id;
    private final StringProperty email;
    private final StringProperty ticketId;
    private final StringProperty userName;

    public Passengers(String id, String fName, String lName, String email, String userName, String ticketId){
        this.id = new SimpleStringProperty(id);
        this.fName = new SimpleStringProperty(fName);
        this.lName = new SimpleStringProperty(lName);
        this.email = new SimpleStringProperty(email);
        this.ticketId = new SimpleStringProperty(ticketId);
        this.userName = new SimpleStringProperty(userName);

    }

    public String getfName() {
        return fName.get();
    }

    public StringProperty fNameProperty() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName.set(fName);
    }

    public String getlName() {
        return lName.get();
    }

    public StringProperty lNameProperty() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName.set(lName);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTicketId() {
        return ticketId.get();
    }

    public StringProperty ticketIdProperty() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId.set(ticketId);
    }

    public String getUserName() { return userName.get(); }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) { this.userName.set(userName);
    }



}
