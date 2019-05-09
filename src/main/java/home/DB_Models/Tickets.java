package main.java.home.DB_Models;

import javafx.beans.property.*;

public class Tickets {


    private IntegerProperty FLIGHTID;
    private IntegerProperty TICKETID;
    private IntegerProperty SOURCEID;
    private IntegerProperty DESTID;
    private IntegerProperty PRICE;
    private BooleanProperty PURCHASED;
    private StringProperty OWNER;



    public Tickets(int flightId, int ticketId, int sourceId, int  destId, int price, boolean purchased, String owner){

        this.FLIGHTID = new SimpleIntegerProperty(flightId);
        this.TICKETID = new SimpleIntegerProperty(ticketId);
        this.SOURCEID = new SimpleIntegerProperty(sourceId);
        this.DESTID = new SimpleIntegerProperty(destId);
        this.PRICE= new SimpleIntegerProperty(price);
        this.PURCHASED = new SimpleBooleanProperty(purchased);
        this.OWNER = new SimpleStringProperty(owner);


    }

    public Tickets(int flightId, int ticketId, int sourceId, int  destId){
        this.FLIGHTID = new SimpleIntegerProperty(flightId);
        this.TICKETID = new SimpleIntegerProperty(ticketId);
        this.SOURCEID = new SimpleIntegerProperty(sourceId);
        this.DESTID = new SimpleIntegerProperty(destId);

    }


    public int getFLIGHTID() {
        return FLIGHTID.get();
    }

    public IntegerProperty FLIGHTIDProperty() {
        return FLIGHTID;
    }

    public void setFLIGHTID(int FLIGHTID) {
        this.FLIGHTID.set(FLIGHTID);
    }

    public int getTICKETID() {
        return TICKETID.get();
    }

    public IntegerProperty TICKETIDProperty() {
        return TICKETID;
    }

    public void setTICKETID(int TICKETID) {
        this.TICKETID.set(TICKETID);
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

    public int getPRICE() {
        return PRICE.get();
    }

    public IntegerProperty PRICEProperty() {
        return PRICE;
    }

    public void setPRICE(int PRICE) {
        this.PRICE.set(PRICE);
    }

    public boolean isPURCHASED() {
        return PURCHASED.get();
    }

    public BooleanProperty PURCHASEDProperty() {
        return PURCHASED;
    }

    public void setPURCHASED(boolean PURCHASED) {
        this.PURCHASED.set(PURCHASED);
    }

    public String getOWNER() {
        return OWNER.get();
    }

    public StringProperty OWNERProperty() {
        return OWNER;
    }

    public void setOWNER(String OWNER) {
        this.OWNER.set(OWNER);
    }
}
