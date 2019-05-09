# DJV_Airlines

A simple airline registration system that facilaties viewing available flights to arguably the top five destinations in the United States, selecting tickets for a desired flight with specific meal and luggage options, the viewing of purchased tickets and the ability to cancel purchases. An administration dashboard
is also available to view passenger lists, add flights and tickets to the airline's database and generate monthly revenue reports.


## System and other requirements
For a full in-depth look at system requirements for JavaFX please read: https://www.oracle.com/technetwork/java/javafx/system-requirements-1-2-140252.html .

I use Intellij IDEA CE as my IDE: https://www.jetbrains.com/idea/ .
External JAR's imported into the project are as follows: 
controlsfx-9.0.0.jar, jfoenix-9.0.6.jar, jbcrypt-0.3m.jar, sqlite-jdbc-3.23.1.jar, and others 
(located in **DJV_Airlines/jar_files**)

Java SE 10 or above, I have Java SE 11 installed on my system: https://www.oracle.com/technetwork/java/javase/downloads/index.html .

JavaFX 11 SDK or above offered by Gluon and OpenJFX among others:
https://gluonhq.com/products/javafx/
https://openjfx.io/openjfx-docs/ .

Gluon SceneBuilder was used to stich together the UI:
https://gluonhq.com/products/scene-builder/ .
External JAR's imported into SceneBuilder via [ Settings -> JAR/FXML Manager -> Add Library/FXML from file system ] are as follows:
controlsfx-9.0.0.jar, jfoenix-9.0.6.jar. (located in **DJV_Airlines/jar_files**)
via [Settings -> JAR/FXML Manager -> Search repositories] search as follows: de.jensd:fontawesomefx-commons:9.1.2, de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2 .


## Project Structure
Down the path **DJV_airlines/src/main/java/home**
there are four main folders, **controllers**, in which each class is the controller of a single page in the application, **DB_Connection**, which holds the class that initiates the conntection to the SQLite database, **DB_Models**, in which each class is the model for a table stored in the airlines' database, and **fxml**, in which each file is the FXML markup of each page of the application. 

The program is launched from **DJV_Airlines/src/main/java/Main.java**


## Future Changes and Updates
The last time I actively worked on this code was right up until this project's due date at the end of my 2018 Fall semester. There are several issues such as buttons that were never given functionality due to time crunches, certain pages such as the layout of the tickets which can be printed are unappealing, the lack of in-depth error handling, etc. During my spare time, I plan to go through the code, learn more, and correct the above issues and more as well as add any additional, practical functionality along with possibly changing certain page layouts and color schemes.



