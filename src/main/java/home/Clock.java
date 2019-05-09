package main.java.home;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Clock {
    //So the constructor will take in the current time and use that to display the time
    private String time;
    private int timeRun = 0;

    public Clock(){
        System.out.println("Clock constructor called");
    }


    //this will be the method that returns the current time//

    public String getTime(){

        //We need a separate thread to so that the clock in the application can update simultaneously along with everything
        //else going on
        //normally a run method would go inside the thread definition but we're using anonymous classes

        new Thread(() -> {
            while(timeRun == 0){
                Calendar cal = new GregorianCalendar();
                //Gets the Hour from the PC clock
                int hour = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);
                int sec = cal.get(Calendar.SECOND);
                int AM_PM = cal.get(Calendar.AM_PM);

                String day_night;

                if(AM_PM == 1){
                    day_night = "PM";
                }else{
                    day_night = "AM";
                }

                time = hour + "" + min + "" + sec + "" +day_night;

                //We're going to set the clock label's text in this method....
                System.out.println("Thread is running");
                System.out.println(time);
            }
        }).start();


        return time;
    }

}
