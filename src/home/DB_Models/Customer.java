package home.DB_Models;

public class Customer {

    /**
     * Below are the different rows which will hold data in the database
     * These rows variables will store data about the customer once the
     * have registered and will get pushed to the database.
     * We may need a seperate credit card class possibly
     */
    private String fName;
    private String lName;
    private String id;
    private String email;
    private String pNumber;
    private String password;



    public Customer(){
        System.out.println("A New customer was created");
    }

    public Customer(String id, String fName, String lName, String email, String password){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
    }

    /**
     * Below are setter methods to create the customer// we shouldn't have to ever use these
     * unless the customer wants to change say their name or email or payment method at some point in time;
     *
     */
    public void setfName(String fName){
        this.fName = fName;
    }
    public void setlName(String lName){
        this.lName = lName;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setpNumber(String pNumber){
        this.pNumber = pNumber;
    }


    /***
     *
     * Below are the getter methods.. these will be used heavily to populate the different views with customer information
     *
     */


    public String getfName(){
        return fName;
    }
    public String getlName(){
        return lName;
    }
    public String getId(){
        return id;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public String getpNumber(){
        return pNumber;
    }

    public String toString(){
        return  "New Customer: " + id +" " + " " + fName + " " + " " + lName + " has been created";
    }

    /***
     *
     * Below we might define methods to say encrypt passwords
     * and anything other things that might come up.
     */
}
