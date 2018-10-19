package home.controllers;

public enum Option {
    Admin, Passenger;

    private Option(){}

    public String value(){
        return name();
    }

    public static Option fromValue(String val){
        return valueOf(val);
    }
}
