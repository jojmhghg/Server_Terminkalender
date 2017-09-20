/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

/**
 *
 * @author Müller_Admin
 */
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
    boolean abfrage;
    private static Connection con;
    private static boolean hasData;
    ResultSet rs;
    
    public DBHandler(){
        abfrage = true;
        hasData = false;
        con = null;
    }
    
    public void displayAuswahl() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
        if(con == null){
            getConnection();
        }
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
            Statement state4 = con.createStatement();
            Statement state5 = con.createStatement();
            
            ResultSet res1 = state1.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            ResultSet res2 = state2.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='termin'");
            ResultSet res3 = state3.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usertermin'");
            ResultSet res4 = state4.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='kontaktliste'");
            ResultSet res5 = state5.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='meldung'");
            
            if(!res1.next() && !res2.next() && !res3.next() && !res4.next() && !res5.next()){
                System.out.println("Building the User table with prepopulated values.");
                Statement stateuser = con.createStatement();
                stateuser.execute("CREATE TABLE user(userID integer,"
                        + "userName varchar(60),"
                        + "eMail varchar(100),"
                        + "name varchar(60),"
                        + "lastname varchar(60),"
                        + "password varchar(60),"
                        + "idCounter integer,"
                        + "PRIMARY KEY (username))");
                
                System.out.println("Building the Termin table with prepopulated values.");
                Statement statetermin = con.createStatement();
                statetermin.execute("CREATE TABLE termin(terminID integer,"
                        + "titel varchar(60),"
                        + "day intger,"
                        + "month integer,"
                        + "year integer,"
                        + "from_hours integer,"
                        + "from_minutes integer,"
                        + "to_hours integer,"
                        + "to_minutes integer,"
                        + "note varchar(100),"
                        + "location varchar(60),"
                        + "ownerID integer,"
                        + "editEveryone integer,"
                        + "foreign key(ownerID) references user(userID),"
                        + "primary key(terminID))");
                
                System.out.println("Building the UserTermin table with prepopulated values.");
                Statement stateusertermin = con.createStatement();
                stateusertermin.execute("CREATE TABLE terminkalender(userID integer,"
                        + "terminID integer,"
                        + "nimmtTeil integer,"
                        + "foreign key(userID) references user(userID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(userID, terminID))");
                
                System.out.println("Building the Kontakliste table with prepopulated values.");
                Statement statekontaktliste = con.createStatement();
                statekontaktliste.execute("CREATE TABLE kontaktliste(UserID integer,"
                        + "kontaktID integer,"
                        + "foreign key(UserID) references user(userID),"
                        + "foreign key(kontaktID) references user(userID),"
                        + "primary key(userID, kontaktID))");
                
                System.out.println("Building the Meldung table with prepopulated values.");
                Statement statefreundschaftsanfrage = con.createStatement();
                statefreundschaftsanfrage.execute("CREATE TABLE meldung(meldungsID integer,"
                        + "userID integer,"
                        + "terminID integer,"
                        + "absenderID integer,"
                        + "gelesen integer,"
                        + "text varchar(100),"
                        + "anfrage integer,"
                        + "foreign key(userID) references meldung(userID),"
                        + "foreign key(terminID) references user(terminID),"
                        + "foreign key(absenderID) references user(userID),"
                        + "primary key(meldungsID))"); 
            }
        }
    }
    
    public void addUser(String username, String passwort, String email, int idCounter, int userID) throws SQLException{
        PreparedStatement prepuser = con.prepareStatement("INSERT INTO user values(?,?,?,?,?,?,?);");
        
        prepuser.setInt(1, userID);
        prepuser.setString(2, username);
        prepuser.setString(3, email);
        prepuser.setString(4, "");
        prepuser.setString(5, "");
        prepuser.setString(6, passwort);
        prepuser.setInt(7, idCounter);
        prepuser.execute(); 
    }
    
    public void resetPassword(String username, String passwort) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET pw = ? WHERE userName = ?");
        prepResetPW.setString(1, passwort);
        prepResetPW.setString(2, username);
        prepResetPW.execute();      
    }
    
    public void addKontakt(int userID, int kontaktID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("INSERT INTO kontaktliste values(?,?);");
        
        prepResetPW.setInt(1, userID);
        prepResetPW.setInt(2, kontaktID);
        prepResetPW.execute();   
    }
    
    public void removeKontakt(int userID, int kontaktID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("DELETE FROM kontaktliste WHERE userID = ? AND kontaktID = ?;");
        
        prepResetPW.setInt(1, userID);
        prepResetPW.setInt(2, kontaktID);
        prepResetPW.execute(); 
    }
    
}