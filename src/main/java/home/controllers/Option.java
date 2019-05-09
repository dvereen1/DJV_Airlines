package main.java.home.controllers;

public enum Option {
    Admin, Passenger;

     Option(){}

    public String value(){
        return name();
    }

    public static Option fromValue(String val){
        return valueOf(val);
    }
}
