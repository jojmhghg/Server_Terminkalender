/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Scanner;

public class DBHandler {
    boolean abfrage = true;
    private static Connection con;
    private static boolean hasData = false;
    ResultSet rs;
    
    public void displayAuswahl() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
        if(con == null){
            getConnection();
        }
        
        showUser();
        showEvent();
        showUserEvent();
    }
    
    private void getConnection() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
       Class.forName("org.sqlite.JDBC");
       con = DriverManager.getConnection("jdbc:sqlite:Kalender.db");
       initialise();   
    }
    
    private void initialise() throws SQLException, NoSuchAlgorithmException{
        if(!hasData){
            hasData = true;
            Statement state1 = con.createStatement();
            Statement state2 = con.createStatement();
            Statement state3 = con.createStatement();
            
            ResultSet res1 = state1.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            ResultSet res2 = state2.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='event'");
            ResultSet res3 = state3.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='userevent'");
            
            if(!res1.next() && !res2.next() && !res3.next()){
                System.out.println("Building the User table with prepopulated values.");
                Statement stateuser = con.createStatement();
                stateuser.execute("CREATE TABLE user(userID integer,"
                        + "fName varchar(60),"
                        + "lName varchar(60),"
                        + "pw varchar(60),"
                        + "primary key(userID))");
                
                System.out.println("Building the Event table with prepopulated values.");
                Statement stateevent = con.createStatement();
                stateevent.execute("CREATE TABLE event(eventID integer,"
                        + "name varchar(60),"
                        + "year intger,"
                        + "month integer,"
                        + "day integer,"
                        + "hours integer,"
                        + "minutes integer,"
                        + "primary key(eventID))");
                
                System.out.println("Building the UserEvent table with prepopulated values.");
                Statement stateuserevent = con.createStatement();
                stateuserevent.execute("CREATE TABLE userevent(usereventID integer,"
                        + "userID integer,"
                        + "eventID integer,"
                        + "foreign key(userID) references user(userID),"
                        + "foreign key(eventID) references event(eventID),"
                        + "primary key(usereventID))");
                }
            }
        while(abfrage){
            dialog();
            System.out.println("--------------------------------------------");
        }
    }
    
    public void dialog() throws SQLException, NoSuchAlgorithmException{
        System.out.print("(1) Neuen Benutzer anlegen\n(2) Neuen Termin anlegen\n(3) User zu Event einladen\n(4) Termine von gewuenschtem User anzeigen\n"
                + "(5) Alle Teilnehmer vom gewuenschten Termin anzeigen\n(6) Alle User anzeigen\n"
                + "(7) Beenden und Ausgeben \nGewuenschte Operation angeben: ");
        Scanner sc = new Scanner(System.in);
        int operation = sc.nextInt();
        
        switch(operation){
            case 1:
                addUser();
                break;
                
            case 2:
                addEvent(whoIsIt());
                break;
                
            case 3:
                addTeilnehmer();
                break;
                
            case 4:
                showTermine();
                break;
                
            case 5:
                showTeilnehmer();
                break;
               
            case 6:
                showUser();
                break;
                
            case 7:
                abfrage = false;
                break;
                
            default:
                System.out.println("Gewuenschte Operation gibt es nicht");
        }
    }
    
    public void addUser() throws SQLException, NoSuchAlgorithmException{
        PreparedStatement prepuser = con.prepareStatement("INSERT INTO user values(?,?,?,?);");
        Scanner sc = new Scanner(System.in);
        System.out.print("Bitte Vorname eingeben: ");
        String fName = sc.next();
        System.out.print("Bitte Nachnamen eingeben: ");
        String lName = sc.next();
        System.out.print("Bitte Passwort eingeben: ");
        String pw = sc.next();
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
        
        byte[] encodedBytes = Base64.getEncoder().encode("hash".getBytes());
        
        String pwHash = new String(encodedBytes, StandardCharsets.UTF_8);
        
        prepuser.setString(2, fName);
        prepuser.setString(3, lName);
        prepuser.setString(4, pwHash);
        prepuser.execute(); 
    }
    
    public void addEvent(int userID) throws SQLException{
        PreparedStatement prepevent = con.prepareStatement("INSERT INTO event values(?,?,?,?,?,?,?);");
        Scanner sc = new Scanner(System.in);
        System.out.print("Bitte Eventname eingeben: ");
        String name = sc.next();
        System.out.print("Bitte Jahr eingeben: ");
        int year = sc.nextInt();
        System.out.print("Bitte Monat eingeben: ");
        int month = sc.nextInt();
        System.out.print("Bitte Tag eingeben: ");
        int day = sc.nextInt();
        System.out.print("Bitte Stunde eingeben: ");
        int hour = sc.nextInt();
        System.out.print("Bitte Minute eingeben: ");
        int minutes = sc.nextInt();
        
        prepevent.setString(2, name);
        prepevent.setInt(3, year);
        prepevent.setInt(4, month);
        prepevent.setInt(5, day);
        prepevent.setInt(6, hour);
        prepevent.setInt(7, minutes);
        prepevent.execute();
        rs = prepevent.getGeneratedKeys();
        int eventID = 0;
            if(rs.next()){
                eventID = rs.getInt(1);  
            }
        userevent(userID, eventID);
    }
    
    public Integer whoIsIt() throws SQLException{
        showUser();
        System.out.print("Bitte ID des Users eingeben: ");
        Scanner sc = new Scanner(System.in);
        int me = sc.nextInt();
        return me;
    }
    
    public void userevent(int userID, int eventID) throws SQLException{
        PreparedStatement prepuserevent = con.prepareStatement("INSERT INTO userevent values(?,?,?);");
        prepuserevent.setInt(2, userID);
        prepuserevent.setInt(3, eventID);
        prepuserevent.execute();
    }
    
    public void showUser() throws SQLException{
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT userID, fname, lname, pw FROM user");       
        System.out.print("User-Tabelle:\n");
        while(res.next()){
            System.out.println(res.getInt("userID") + " " + res.getString("fname") + " " + res.getString("lname") + " " + res.getString("pw"));
        }
        System.out.print("--------------------------------------------\n");
    }
    
    public void showEvent() throws SQLException{
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT eventID, name, year, month, day, hours, minutes FROM event");
        System.out.print("Event-Tabelle:\n");
        while(res.next()){
            System.out.println(res.getInt("eventID") + " " + res.getString("name") + " " + res.getInt("year") + " " + res.getInt("month") + " " + res.getInt("day") 
                    + " " + res.getInt("hours") + ":" + res.getInt("minutes") + "Uhr");
        }
        System.out.print("--------------------------------------------\n");
    }
    
    public void showUserEvent() throws SQLException{
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT userID, eventID FROM userevent");
        System.out.print("UserEvent-Tabelle:\n");
        while(res.next()){
            System.out.println(res.getInt("userID") + " " + res.getInt("eventID"));
        }
    }
    
    public void showTermine() throws SQLException{
        Scanner sc = new Scanner(System.in);
        showUser();
        System.out.print("Bitte ID von dem gewuenschten User eingeben:");
        int id = sc.nextInt();
        System.out.print("-----------\n");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * From event as ev\n" +
        "Join userevent as usev on ev.eventID = usev.eventID\n" +
        "Where usev.userID = " + id);
        while(res.next()){
            System.out.println(res.getInt("eventID") + " " + res.getString("name") + " " + res.getInt("year") + " " + res.getInt("month") + " " + res.getInt("day") 
                            + " " + res.getInt("hours") + ":" + res.getInt("minutes") + "Uhr");
        }
    }
    
    public void showTeilnehmer() throws SQLException{
        Scanner sc = new Scanner(System.in);
        showEvent();
        System.out.print("Bitte ID von dem gewuenschten Event eingeben:");
        int id = sc.nextInt();
        System.out.print("-----------\n");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM user as us\n" +
                "Join userevent as usev on us.userID = usev.userID\n" +
                "Where usev.eventID = " + id);
        while(res.next()){
            System.out.println(res.getInt("userID") + " " + res.getString("fname") + " " + res.getString("lname") + " " + res.getString("pw"));
        }
    }
    
    public void addTeilnehmer() throws SQLException{
        boolean check = true;
        Scanner sc = new Scanner(System.in);
        while(check){
            showUser();
            System.out.print("Bitte ID von dem gewuenschten User eingeben:");
            int uid = sc.nextInt();
            showEvent();
            System.out.print("Bitte ID von dem gewuenschten Event eingeben:");
            int eid = sc.nextInt();
            userevent(uid, eid);
            check = false;
        }   
    }
}