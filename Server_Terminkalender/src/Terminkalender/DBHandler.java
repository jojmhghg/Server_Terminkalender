/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

/**
 *
 * @author timtim
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
                
                System.out.println("Building the MeldungsTyp table with prepopulated values.");
                Statement statemeldungstyp = con.createStatement();
                statemeldungstyp.execute("CREATE TABLE meldungstyp(meldungsTypID integer,"
                        + "userID integer,"
                        + "anfrage integer,"
                        + "notiz varchar(60)"
                        + "foreign key(userID) references user(userID),"
                        + "primary key(meldungsTypID");
                
                System.out.println("Building the Meldung table with prepopulated values.");
                Statement statemeldung = con.createStatement();
                statemeldung.execute("CREATE TABLE meldung(meldungsTypID integer,"
                        + "text varchar(60),"
                        + "terminID integer,"
                        + "gelesen integer,"
                        + "anfrage integer,"
                        + "foreign key(meldungsTypID) references meldungstyp(meldungsTypID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(meldungsTypID))");
                        
                System.out.println("Building the MeldungAnfrage table with prepopulated values.");
                Statement statemeldunganfrage = con.createStatement();
                statemeldung.execute("CREATE TABLE meldungsanfrage(meldungsTypID integer,"
                        + "text varchar(60),"
                        + "terminID integer,"
                        + "gelesen integer,"
                        + "absenderID integer,"
                        + "foreign key(terminID) references termin(terminID),"
                        + "foreign key(meldungsTypID) references meldungstyp(meldungsTypID),"
                        + "foreign key(absenderID) references user(userID),"
                        + "primary key(meldungsTypID");
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
        PreparedStatement prepResetPW = con.prepareStatement("UPDATE user SET p = ? WHERE userName = ?");
        prepResetPW.setString(1, passwort);
        prepResetPW.setString(2, username);
        prepResetPW.execute();      
    }
    
    public void addKontakt(int userID, int kontaktID) throws SQLException{
        PreparedStatement prepAddKontakt = con.prepareStatement("INSERT INTO kontaktliste values(?,?);");
        prepAddKontakt.setInt(1, userID);
        prepAddKontakt.setInt(2, kontaktID);
        prepAddKontakt.execute();   
    }
    
    public void removeKontakt(int userID, int kontaktID) throws SQLException{
        PreparedStatement prepRemoveKontakt = con.prepareStatement("DELETE FROM kontaktliste WHERE userID = ? AND kontaktID = ?;");
        
        prepRemoveKontakt.setInt(1, userID);
        prepRemoveKontakt.setInt(2, kontaktID);
        prepRemoveKontakt.execute(); 
    }
    
    public void changePasswort(String neuesPW, int userID) throws SQLException{
        PreparedStatement prepChangePasswort = con.prepareStatement("UPDATE user SET password = ? WHERE userID = ?");
        prepChangePasswort.setString(1, neuesPW);
        prepChangePasswort.setInt(2, userID);
        prepChangePasswort.execute(); 
    }
    
    public void changeVorname(String neuerVorname, int userID) throws SQLException{
        PreparedStatement prepChangeVorname = con.prepareStatement("UPDATE user SET name = ? WHERE userID = ?");
        prepChangeVorname.setString(1, neuerVorname);
        prepChangeVorname.setInt(2, userID);
        prepChangeVorname.execute(); 
    }
    
    public void changeNachname(String neuerNachname, int userID) throws SQLException{
        PreparedStatement prepChangeNachname = con.prepareStatement("UPDATE user SET lastname = ? WHERE userID = ?");
        prepChangeNachname.setString(1, neuerNachname);
        prepChangeNachname.setInt(2, userID);
        prepChangeNachname.execute(); 
    }
    
    public void changeEmail(String neueEmail, int userID) throws SQLException{
        PreparedStatement prepChangeEmail = con.prepareStatement("UPDATE user SET eMail = ? WHERE userID = ?");
        prepChangeEmail.setString(1, neueEmail);
        prepChangeEmail.setInt(2, userID);
        prepChangeEmail.execute(); 
    }
    
    public void addnewTermin(Datum datum, Zeit beginn, Zeit ende, String titel, int terminID, int userID) throws SQLException{
        PreparedStatement prepAddNewTermin = con.prepareStatement("INSERT INTO termin values(?,?,?,?,?,?,?,?,?,?,?,?,?);");
        prepAddNewTermin.setInt(1, terminID);
        prepAddNewTermin.setString(2, titel);
        prepAddNewTermin.setInt(3, datum.getTag());
        prepAddNewTermin.setInt(4, datum.getMonat());
        prepAddNewTermin.setInt(5, datum.getJahr());
        prepAddNewTermin.setInt(6, beginn.getStunde());
        prepAddNewTermin.setInt(7, beginn.getMinute());
        prepAddNewTermin.setInt(8, ende.getStunde());
        prepAddNewTermin.setInt(9, ende.getMinute());
        prepAddNewTermin.setString(10, "");
        prepAddNewTermin.setString(11, "");
        prepAddNewTermin.setInt(12, userID);
        prepAddNewTermin.setInt(13, 1);
        prepAddNewTermin.execute();
        addTermin(terminID, userID, 1);
    }
    
    public void addTermin(int terminID, int userID, int nimmtTeil) throws SQLException{
        PreparedStatement prepAddTermin = con.prepareStatement("INSERT INTO terminkalender values(?,?,?);");
        prepAddTermin.setInt(1, userID);
        prepAddTermin.setInt(2, terminID);
        prepAddTermin.setInt(3, nimmtTeil);
        prepAddTermin.execute();
    }
    
    public void removeTermin(int terminID, int userID) throws SQLException{
        PreparedStatement prepRemoveTermin = con.prepareStatement("DELETE FROM terminkalender WHERE userID = ? AND terminID = ?;");      
        prepRemoveTermin.setInt(1, userID);
        prepRemoveTermin.setInt(2, terminID);
        prepRemoveTermin.execute(); 
    }
    
    public void deleteTermin(int terminID) throws SQLException{
        PreparedStatement prepDeleteTermin = con.prepareStatement("DELETE FROM terminkalender WHERE terminID = ?;");      
        prepDeleteTermin.setInt(1, terminID);
        prepDeleteTermin.execute(); 
        
        PreparedStatement deleteTermin = con.prepareStatement("DELETE FROM termin WHERE terminID = ?;");      
        deleteTermin.setInt(1, terminID);
        deleteTermin.execute(); 
    }
    
    public void changeEditierrechte(boolean editierbar, int terminID) throws SQLException{
        PreparedStatement prepChangeEditierrechte = con.prepareStatement("UPDATE termin SET editEveryone = ? WHERE terminID = ?");
        if(editierbar){
            prepChangeEditierrechte.setInt(1, 1);
        }
        else{
            prepChangeEditierrechte.setInt(1, 0);
        }
        prepChangeEditierrechte.setInt(2, terminID);
        prepChangeEditierrechte.execute();
    }
            
    public void changeTerminort(int terminID, String neuerOrt) throws SQLException{
        PreparedStatement prepChangeTerminort = con.prepareStatement("UPDATE termin SET location = ? WHERE terminID = ?");
        prepChangeTerminort.setString(1, neuerOrt);
        prepChangeTerminort.setInt(2, terminID);
        prepChangeTerminort.execute();
    }
    
    public void changeTermintitel(int terminID, String neuerTitel) throws SQLException{
        PreparedStatement prepChangeTermintitel = con.prepareStatement("UPDATE termin SET titel = ? WHERE terminID = ?");
        prepChangeTermintitel.setString(1, neuerTitel);
        prepChangeTermintitel.setInt(2, terminID);
        prepChangeTermintitel.execute();
    }
    
    public void changeTerminnotiz(int terminID, String neueNotiz) throws  SQLException{
        PreparedStatement prepChangeTerminnotiz = con.prepareStatement("UPDATE termin SET note = ? WHERE terminID = ?");
        prepChangeTerminnotiz.setString(1, neueNotiz);
        prepChangeTerminnotiz.setInt(2, terminID);
        prepChangeTerminnotiz.execute();
    }
    
    public void changeTerminende(int terminID, Zeit neuesEnde) throws  SQLException{
        PreparedStatement prepChangeTerminende = con.prepareStatement("UPDATE termin SET to_hours = ?, to_minutes = ? WHERE terminID = ?");
        prepChangeTerminende.setInt(1, neuesEnde.getStunde());
        prepChangeTerminende.setInt(2, neuesEnde.getMinute());
        prepChangeTerminende.setInt(3, terminID);
        prepChangeTerminende.execute(); 
    }
    
    public void changeTerminbeginn(int terminID, Zeit neuerBeginn) throws SQLException{
        PreparedStatement prepChangeTerminbeginn = con.prepareStatement("UPDATE termin SET from_hours = ?, from_minutes = ? WHERE terminID = ?");
        prepChangeTerminbeginn.setInt(1, neuerBeginn.getStunde());
        prepChangeTerminbeginn.setInt(2, neuerBeginn.getMinute());
        prepChangeTerminbeginn.setInt(3, terminID);
        prepChangeTerminbeginn.execute(); 
    }
    
    public void changeTermindatum(int terminID, Datum neuesDatum) throws SQLException{
        PreparedStatement prepChangeTermin = con.prepareStatement("UPDATE termin SET day = ?, month = ?, year = ? WHERE terminID = ?");
        prepChangeTermin.setInt(1, neuesDatum.getTag());
        prepChangeTermin.setInt(2, neuesDatum.getMonat());
        prepChangeTermin.setInt(3, neuesDatum.getJahr());
        prepChangeTermin.setInt(4, terminID);
        prepChangeTermin.execute(); 
    }
    
    public void nimmtTeil(int terminID, int userID) throws SQLException{
        PreparedStatement prepNimmtTeil = con.prepareStatement("UPDATE terminkalender SET nimmtTeil = ? WHERE terminID = ? AND userID = ?");
        prepNimmtTeil.setInt(1, 1);
        prepNimmtTeil.setInt(2, terminID);
        prepNimmtTeil.setInt(3, userID);
        prepNimmtTeil.execute();
    }
    
    public void addMeldung(int meldungsID, int userID, String text) throws SQLException{
        PreparedStatement prepAddMeldung = con.prepareStatement("INSERT INTO meldung values(?,?,?,?,?,?,?);");
        prepAddMeldung.setInt(1, meldungsID);
        prepAddMeldung.setInt(2, userID);
        prepAddMeldung.setString(3, text);
        prepAddMeldung.setInt(4, 0);
        prepAddMeldung.setInt(5, 0);
        prepAddMeldung.setInt(6, 0);
        prepAddMeldung.setInt(7, 0);
        prepAddMeldung.execute();
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT meldungsCounter FROM user" +
                "Where userID = " + userID);
        int meldungsCounter = res.getInt("meldungsCounter");
        PreparedStatement prepAddMeldungsCounter = con.prepareStatement("INSERT INTO user values(?,?,?,?,?,?,?);");
        prepAddMeldungsCounter.setInt(7, (meldungsCounter + 1));
        prepAddMeldungsCounter.execute();
    }
    
    public void addAnfrage(int anfrageID, int userID, int terminID, int absenderID) throws SQLException{
        PreparedStatement prepAddAnfrage = con.prepareStatement("INSERT INTO meldung values(?,?,?,?,?,?,?);");
        prepAddAnfrage.setInt(1, anfrageID);
        prepAddAnfrage.setInt(2, userID);
        prepAddAnfrage.setString(3, "");
        prepAddAnfrage.setInt(4, terminID);
        prepAddAnfrage.setInt(5, absenderID);
        prepAddAnfrage.setInt(6, 0);
        prepAddAnfrage.setInt(7, 1);
        prepAddAnfrage.execute();
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT meldungsCounter, idCounter FROM user" +
                "Where userID = " + userID);
        int meldungsCounter = res.getInt("meldungsCounter");
        int idCounter = res.getInt("idCounter");
        PreparedStatement prepAddMeldungsCounter = con.prepareStatement("INSERT INTO user values(?,?,?,?,?,?,?,?);");
        prepAddMeldungsCounter.setInt(7, (meldungsCounter + 1));
        prepAddMeldungsCounter.setInt(8, (idCounter + 1));
        prepAddMeldungsCounter.execute();
    }
    
    
    public void deleteMeldung(int index, int sitzungsID) throws SQLException{
        
    }
    
    public void setMeldungenGelesen(int index, int sitzungsID) throws SQLException{
        
    }
    
    public ResultSet showTermine(int userID) throws SQLException{
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * From termin" +
        "Join usertermin on termin.terminID = usertermin.terminID" +
        "Where usertermin.userID = " + userID);
        return res;
    }
    
    public ResultSet showUser(int userID) throws SQLException{
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * FROM user" +
                "Where userID = " + userID);
        return res;
    }
    
    public ResultSet showKontaktliste(int userID) throws SQLException{
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * FROM kontaktliste" +
                "Where userID = " + userID);
        return res;
    }
    
    public ResultSet showMeldungen(int userID) throws SQLException{
        Statement state = con.createStatement();
        ResultSet res2 = null;
        ResultSet res = state.executeQuery("Select * FROM meldungstyp" +
                "Where userID = " + userID +
                "Where anfrage =" + 0);
        while(res.next()){
            res2 = state.executeQuery("Select * FROM meldung" +
                    "Where meldungsTypID = " + res.getInt("meldungsTypID"));
            }
        return res2;
    }
    
    public ResultSet showAnfragen(int userID) throws SQLException{
        Statement state = con.createStatement();
        ResultSet res2 = null;
        ResultSet res = state.executeQuery("Select * FROM meldungstyp" +
                "Where userID = " + userID +
                "Where anfrage =" + 1);
        while(res.next()){
            res2 = state.executeQuery("Select * FROM meldungsanfrage" +
                    "Where meldungsTypID = " + res.getInt("meldungsTypID"));
            }
        return res2;
    }
    
    public void showBenutzer(int userID) throws SQLException{
        ResultSet termin = showTermine(userID);
        ResultSet user = showUser(userID);
        ResultSet kontaktliste = showKontaktliste(userID);
        ResultSet meldungen = showMeldungen(userID);
        ResultSet anfragen = showAnfragen(userID);
    }
  
}
