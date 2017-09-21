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
                        + "meldungsCounter integer,"
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
                        + "text varchar(60),"
                        + "terminID integer,"
                        + "absenderID integer,"
                        + "gelesen integer,"
                        + "anfrage integer,"
                        + "foreign key(userID) references meldung(userID),"
                        + "foreign key(terminID) references user(terminID),"
                        + "foreign key(absenderID) references user(userID),"
                        + "primary key(meldungsID))"); 
            }
        }
    }
    
    public void addUser(String username, String passwort, String email, int meldungsCounter, int userID) throws SQLException{
        PreparedStatement prepuser = con.prepareStatement("INSERT INTO user values(?,?,?,?,?,?,?);");        
        prepuser.setInt(1, userID);
        prepuser.setString(2, username);
        prepuser.setString(3, email);
        prepuser.setString(4, "");
        prepuser.setString(5, "");
        prepuser.setString(6, passwort);
        prepuser.setInt(7, meldungsCounter);
        prepuser.execute(); 
    }
    
    public void resetPassword(String username, String passwort) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET password = ? WHERE userName = ?");
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
    
    public void changePasswort(String neuesPW, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET password = ? WHERE userID = ?");
        prepResetPW.setString(1, neuesPW);
        prepResetPW.setInt(2, userID);
        prepResetPW.execute(); 
    }
    
    public void changeVorname(String neuerVorname, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET name = ? WHERE userID = ?");
        prepResetPW.setString(1, neuerVorname);
        prepResetPW.setInt(2, userID);
        prepResetPW.execute(); 
    }
    
    public void changeNachname(String neuerNachname, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET lastname = ? WHERE userID = ?");
        prepResetPW.setString(1, neuerNachname);
        prepResetPW.setInt(2, userID);
        prepResetPW.execute(); 
    }
    
    public void changeEmail(String neueEmail, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET eMail = ? WHERE userID = ?");
        prepResetPW.setString(1, neueEmail);
        prepResetPW.setInt(2, userID);
        prepResetPW.execute(); 
    }
    
    public void addnewTermin(Datum datum, Zeit beginn, Zeit ende, String titel, int terminID, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("INSERT INTO termin values(?,?,?,?,?,?,?,?,?,?,?,?,?);");
        prepResetPW.setInt(1, terminID);
        prepResetPW.setString(2, titel);
        prepResetPW.setInt(3, datum.getTag());
        prepResetPW.setInt(4, datum.getMonat());
        prepResetPW.setInt(5, datum.getJahr());
        prepResetPW.setInt(6, beginn.getStunde());
        prepResetPW.setInt(7, beginn.getMinute());
        prepResetPW.setInt(8, ende.getStunde());
        prepResetPW.setInt(9, ende.getMinute());
        prepResetPW.setString(10, "");
        prepResetPW.setString(11, "");
        prepResetPW.setInt(12, userID);
        prepResetPW.setInt(13, 1);
       
        prepResetPW.execute();
        addTermin(terminID, userID, 1);
    }
    
    public void addTermin(int terminID, int userID, int nimmtTeil) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("INSERT INTO terminkalender values(?,?,?);");
        prepResetPW.setInt(1, userID);
        prepResetPW.setInt(2, terminID);
        prepResetPW.setInt(3, nimmtTeil);
        prepResetPW.execute();
    }
    
    public void removeTermin(int terminID, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("DELETE FROM terminkalender WHERE userID = ? AND terminID = ?;");      
        prepResetPW.setInt(1, userID);
        prepResetPW.setInt(2, terminID);
        prepResetPW.execute(); 
    }
    
    public void deleteTermin(int terminID) throws SQLException{
        PreparedStatement removeTermin = con.prepareStatement("DELETE FROM terminkalender WHERE terminID = ?;");      
        removeTermin.setInt(1, terminID);
        removeTermin.execute(); 
        
        PreparedStatement deleteTermin = con.prepareStatement("DELETE FROM termin WHERE terminID = ?;");      
        deleteTermin.setInt(1, terminID);
        deleteTermin.execute(); 
    }
    
    public void changeEditierrechte(boolean editierbar, int terminID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET editEveryone = ? WHERE terminID = ?");
        if(editierbar){
            prepResetPW.setInt(1, 1);
        }
        else{
            prepResetPW.setInt(1, 0);
        }
        prepResetPW.setInt(2, terminID);
        prepResetPW.execute();
    }
            
    public void changeTerminort(int terminID, String neuerOrt) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET location = ? WHERE terminID = ?");
        prepResetPW.setString(1, neuerOrt);
        prepResetPW.setInt(2, terminID);
        prepResetPW.execute();
    }
    
    public void changeTermintitel(int terminID, String neuerTitel) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET titel = ? WHERE terminID = ?");
        prepResetPW.setString(1, neuerTitel);
        prepResetPW.setInt(2, terminID);
        prepResetPW.execute();
    }
    
    public void changeTerminnotiz(int terminID, String neueNotiz) throws  SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET note = ? WHERE terminID = ?");
        prepResetPW.setString(1, neueNotiz);
        prepResetPW.setInt(2, terminID);
        prepResetPW.execute();
    }
    
    public void changeTerminende(int terminID, Zeit neuesEnde) throws  SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET to_hours = ?, to_minutes = ? WHERE terminID = ?");
        prepResetPW.setInt(1, neuesEnde.getStunde());
        prepResetPW.setInt(2, neuesEnde.getMinute());
        prepResetPW.setInt(3, terminID);
        prepResetPW.execute(); 
    }
    
    public void changeTerminbeginn(int terminID, Zeit neuerBeginn) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET from_hours = ?, from_minutes = ? WHERE terminID = ?");
        prepResetPW.setInt(1, neuerBeginn.getStunde());
        prepResetPW.setInt(2, neuerBeginn.getMinute());
        prepResetPW.setInt(3, terminID);
        prepResetPW.execute(); 
    }
    
    public void changeTermindatum(int terminID, Datum neuesDatum) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE termin SET day = ?, month = ?, year = ? WHERE terminID = ?");
        prepResetPW.setInt(1, neuesDatum.getTag());
        prepResetPW.setInt(2, neuesDatum.getMonat());
        prepResetPW.setInt(3, neuesDatum.getJahr());
        prepResetPW.setInt(4, terminID);
        prepResetPW.execute(); 
    }
    
    public void nimmtTeil(int terminID, int userID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE terminkalender SET nimmtTeil = 1 WHERE terminID = ? AND userID = ?");
        prepResetPW.setInt(1, terminID);
        prepResetPW.setInt(2, userID);
        prepResetPW.execute();
    }
    
    public void addMeldung(int meldungsID, int userID, String text) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("INSERT INTO meldung values(?,?,?,?,?,?,?);");
        prepResetPW.setInt(1, meldungsID);
        prepResetPW.setInt(2, userID);
        prepResetPW.setString(3, text);
        prepResetPW.setInt(4, 0);
        prepResetPW.setInt(5, 0);
        prepResetPW.setInt(6, 0);
        prepResetPW.setInt(7, 0);
        prepResetPW.execute();
    }
    
    public void addAnfrage(int anfrageID, int userID, int terminID, int absenderID) throws SQLException{
        PreparedStatement prepResetPW = con.prepareStatement("INSERT INTO meldung values(?,?,?,?,?,?);");
        prepResetPW.setInt(1, anfrageID);
        prepResetPW.setInt(2, userID);
        prepResetPW.setString(3, "");
        prepResetPW.setInt(4, terminID);
        prepResetPW.setInt(5, absenderID);
        prepResetPW.setInt(6, 0);
        prepResetPW.setInt(7, 1);
        prepResetPW.execute();
    }
    
    public void deleteMeldung(int index, int sitzungsID) throws SQLException{
        
    }
    
    public void setMeldungenGelesen(int index, int sitzungsID) throws SQLException{
        
    }
}