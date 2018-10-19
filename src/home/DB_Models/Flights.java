package home.DB_Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Flights {

    private  IntegerProperty ID;
    private StringProperty NAME;
    private  IntegerProperty SOURCEID;
    private IntegerProperty DESTID;
    private StringProperty DEPARTDATE;
    private  StringProperty ARRIVALDATE;
    private StringProperty ARRIVALTIME;
    private  StringProperty DEPARTTIME ;



    public Flights(int id, String name, int sourceId, int  destId, String departDate, String departTime, String arrivalDate, String arrivalTime ){

        this.ID = new SimpleIntegerProperty(id);
        this.NAME =new SimpleStringProperty(name);
        this.SOURCEID = new SimpleIntegerProperty(sourceId);
        this.DESTID = new SimpleIntegerProperty(destId);
        this.DEPARTDATE = new SimpleStringProperty(departDate);
        this.ARRIVALDATE = new SimpleStringProperty(arrivalDate);
        this.ARRIVALTIME = new SimpleStringProperty(arrivalTime);
        this.DEPARTTIME = new SimpleStringProperty(departTime);

    }

    public Flights(String name, String departTime, String arrivalTime){
        this.NAME = new SimpleStringProperty(name);
        this.DEPARTTIME = new SimpleStringProperty(departTime);
        this.ARRIVALTIME = new SimpleStringProperty(arrivalTime);
    }

    public int getID() {
        return ID.get();
    }

    public IntegerProperty IDProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getNAME() {
        return NAME.get();
    }

    public StringProperty NAMEProperty() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME.set(NAME);
    }

    public int getSOURCEID() {
        return SOURCEID.get();
    }

    public IntegerProperty SOURCEIDProperty() {
        return SOURCEID;
    }

    public void setSOURCEID(int SOURCEID) {
        this.SOURCEID.set(SOURCEID);
    }

    public int getDESTID() {
        return DESTID.get();
    }

    public IntegerProperty DESTIDProperty() {
        return DESTID;
    }

    public void setDESTID(int DESTID) {
        this.DESTID.set(DESTID);
    }

    public String getDEPARTDATE() {
        return DEPARTDATE.get();
    }

    public StringProperty DEPARTDATEProperty() {
        return DEPARTDATE;
    }

    public void setDEPARTDATE(String DEPARTDATE) {
        this.DEPARTDATE.set(DEPARTDATE);
    }

    public String getARRIVALDATE() {
        return ARRIVALDATE.get();
    }

    public StringProperty ARRIVALDATEProperty() {
        return ARRIVALDATE;
    }

    public void setARRIVALDATE(String ARRIVALDATE) {
        this.ARRIVALDATE.set(ARRIVALDATE);
    }

    public String getARRIVALTIME() {
        return ARRIVALTIME.get();
    }

    public StringProperty ARRIVALTIMEProperty() {
        return ARRIVALTIME;
    }

    public void setARRIVALTIME(String ARRIVALTIME) {
        this.ARRIVALTIME.set(ARRIVALTIME);
    }

    public String getDEPARTTIME() {
        return DEPARTTIME.get();
    }

    public StringProperty DEPARTTIMEProperty() {
        return DEPARTTIME;
    }

    public void setDEPARTTIME(String DEPARTTIME) {
        this.DEPARTTIME.set(DEPARTTIME);
    }





}
