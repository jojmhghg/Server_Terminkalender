/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terminkalender;

/**
 *
 * @author Müller_Admin
 */
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
        showTermin();
        showUserTermin();
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
            Statement state6 = con.createStatement();
            Statement state7 = con.createStatement();
            Statement state8 = con.createStatement();
            Statement state9 = con.createStatement();
            Statement state10 = con.createStatement();
            
            ResultSet res1 = state1.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'");
            ResultSet res2 = state2.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='termin'");
            ResultSet res3 = state3.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usertermin'");
            ResultSet res4 = state4.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='kontaktliste'");
            ResultSet res5 = state5.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='meldung'");
            ResultSet res6 = state6.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='meldungsTyp'");
            ResultSet res7 = state7.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='freundschaftsanfrage'");
            ResultSet res8 = state8.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='termineinladung'");
            ResultSet res9 = state9.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='terminabsage'");
            ResultSet res10 = state10.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='terminrauswurf'");
            
            if(!res1.next() && !res2.next() && !res3.next() && !res4.next() && !res5.next() && !res6.next() && !res7.next() && !res8.next() && !res9.next() && !res10.next()){
                System.out.println("Building the User table with prepopulated values.");
                Statement stateuser = con.createStatement();
                stateuser.execute("CREATE TABLE user(userID integer,"
                        + "userName varchar(60),"
                        + "eMail varchar(100),"
                        + "fName varchar(60),"
                        + "lName varchar(60),"
                        + "pw varchar(60),"
                        + "idCounter integer,"
                        + "primary key(userID))");
                
                System.out.println("Building the Termin table with prepopulated values.");
                Statement statetermin = con.createStatement();
                statetermin.execute("CREATE TABLE termin(terminID integer,"
                        + "name varchar(60),"
                        + "year intger,"
                        + "month integer,"
                        + "day integer,"
                        + "hours integer,"
                        + "minutes integer,"
                        + "wieLang integer,"
                        + "notiz varchar(100),"
                        + "location varchar(60),"
                        + "ownerID integer,"
                        + "editEveryone integer,"
                        + "foreign key(ownerID) references user(userID),"
                        + "primary key(terminID))");
                
                System.out.println("Building the UserTermin table with prepopulated values.");
                Statement stateusertermin = con.createStatement();
                stateusertermin.execute("CREATE TABLE usertermin(userterminID integer,"
                        + "userID integer,"
                        + "terminID integer,"
                        + "foreign key(userID) references user(userID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(userterminID))");
                
                System.out.println("Building the Kontakliste table with prepopulated values.");
                Statement statekontaktliste = con.createStatement();
                statekontaktliste.execute("CREATE TABLE kontaktliste(fromUserID integer,"
                        + "kontaktUserID integer,"
                        + "foreign key(fromUserID) references user(userID),"
                        + "foreign key(kontaktUserID) references user(userID))");
                
                System.out.println("Building the Meldung table with prepopulated values.");
                Statement statemeldung = con.createStatement();
                statemeldung.execute("CREATE TABLE meldung(meldungID integer,"
                        + "meldungsTypID integer(100),"
                        + "primary key(meldungID),"
                        + "foreign key(meldungsTypID) references user(userID))");
                
                System.out.println("Building the MeldungsTyp table with prepopulated values.");
                Statement statemeldungstyp = con.createStatement();
                statemeldungstyp.execute("CREATE TABLE meldungstyp(meldungsTypID integer,"
                        + "name varchar(60),"
                        + "primary key(meldungsTypID))");
                
                System.out.println("Building the MeldungFreundschaftsanfragen table with prepopulated values.");
                Statement statefreundschaftsanfrage = con.createStatement();
                statefreundschaftsanfrage.execute("CREATE TABLE meldungfreundschaftsanfrage(meldungID integer,"
                        + "anfrageUserID integer,"
                        + "angefragterUserID integer,"
                        + "foreign key(meldungID) references meldung(meldungID),"
                        + "foreign key(anfrageUserID) references user(userID),"
                        + "foreign key(angefragterUserID) references user(userID),"
                        + "primary key(meldungID, anfrageUserID, angefragterUserID))");
                
                System.out.println("Building the MeldungTermineinladung table with prepopulated values.");
                Statement statetermineinladung = con.createStatement();
                statetermineinladung.execute("CREATE TABLE meldungtermineinladung(meldungID integer,"
                        + "anfrageUserID integer,"
                        + "terminID integer,"
                        + "foreign key(meldungID) references meldung(meldungID),"
                        + "foreign key(anfrageUserID) references user(userID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(meldungID, anfrageUserID, terminID))");
                
                System.out.println("Building the MeldungTerminabsage table with prepopulated values.");
                Statement stateterminabsage = con.createStatement();
                stateterminabsage.execute("CREATE TABLE meldungterminabsage(meldungID integer,"
                        + "absageUserID integer,"
                        + "terminID integer,"
                        + "foreign key(meldungID) references meldung(meldungID),"
                        + "foreign key(absageUserID) references user(userID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(meldungID, absageUserID, terminID))");
                
                System.out.println("Building the MeldunTerminrauswurf table with prepopulated values.");
                Statement stateterminrauswurf = con.createStatement();
                stateterminrauswurf.execute("CREATE TABLE meldungterminrauswurf(meldungID integer,"
                        + "rausgeworfenerUserID integer,"
                        + "terminID integer,"
                        + "foreign key(meldungID) references meldung(meldungID),"
                        + "foreign key(rausgeworfenerUserID) references user(userID),"
                        + "foreign key(terminID) references termin(terminID),"
                        + "primary key(meldungID, rausgeworfenerUserID, terminID))");
                }
            }
            while(abfrage){
            dialog();
            System.out.println("--------------------------------------------");
        }
    }
    
    public void dialog() throws SQLException, NoSuchAlgorithmException{
        System.out.print("(1) Neuen Benutzer anlegen\n(2) Neuen Termin anlegen\n(3) User zu Termin einladen\n(4) Termine von gewuenschtem User anzeigen\n"
                + "(5) Alle Teilnehmer vom gewuenschten Termin anzeigen\n(6) Alle User anzeigen\n"
                + "(7) Beenden und Ausgeben \nGewuenschte Operation angeben: ");
        Scanner sc = new Scanner(System.in);
        int operation = sc.nextInt();
        
        switch(operation){
            case 1:
                addUser();
                break;
                
            case 2:
                addTermin(whoIsIt());
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
    
    public void addTermin(int userID) throws SQLException{
        PreparedStatement preptermin = con.prepareStatement("INSERT INTO termin values(?,?,?,?,?,?,?);");
        Scanner sc = new Scanner(System.in);
        System.out.print("Bitte Terminname eingeben: ");
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
        
        preptermin.setString(2, name);
        preptermin.setInt(3, year);
        preptermin.setInt(4, month);
        preptermin.setInt(5, day);
        preptermin.setInt(6, hour);
        preptermin.setInt(7, minutes);
        preptermin.execute();
        rs = preptermin.getGeneratedKeys();
        int terminID = 0;
            if(rs.next()){
                terminID = rs.getInt(1);  
            }
        usertermin(userID, terminID);
    }
    
    public Integer whoIsIt() throws SQLException{
        showUser();
        System.out.print("Bitte ID des Users eingeben: ");
        Scanner sc = new Scanner(System.in);
        int me = sc.nextInt();
        return me;
    }
    
    public void usertermin(int userID, int terminID) throws SQLException{
        PreparedStatement prepusertermin = con.prepareStatement("INSERT INTO usertermin values(?,?,?);");
        prepusertermin.setInt(2, userID);
        prepusertermin.setInt(3, terminID);
        prepusertermin.execute();
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
    
    public void showTermin() throws SQLException{
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT terminID, name, year, month, day, hours, minutes FROM termin");
        System.out.print("Termin-Tabelle:\n");
        while(res.next()){
            System.out.println(res.getInt("terminID") + " " + res.getString("name") + " " + res.getInt("year") + " " + res.getInt("month") + " " + res.getInt("day") 
                    + " " + res.getInt("hours") + ":" + res.getInt("minutes") + "Uhr");
        }
        System.out.print("--------------------------------------------\n");
    }
    
    public void showUserTermin() throws SQLException{
        Statement state = con.createStatement();
        ResultSet res;
        res = state.executeQuery("SELECT userID, terminID FROM usertermin");
        System.out.print("UserTermin-Tabelle:\n");
        while(res.next()){
            System.out.println(res.getInt("userID") + " " + res.getInt("terminID"));
        }
    }
    
    public void showTermine() throws SQLException{
        Scanner sc = new Scanner(System.in);
        showUser();
        System.out.print("Bitte ID von dem gewuenschten User eingeben:");
        int id = sc.nextInt();
        System.out.print("-----------\n");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("Select * From termin as ev\n" +
        "Join usertermin as usev on ev.terminID = usev.terminID\n" +
        "Where usev.userID = " + id);
        while(res.next()){
            System.out.println(res.getInt("terminID") + " " + res.getString("name") + " " + res.getInt("year") + " " + res.getInt("month") + " " + res.getInt("day") 
                            + " " + res.getInt("hours") + ":" + res.getInt("minutes") + "Uhr");
        }
    }
    
    public void showTeilnehmer() throws SQLException{
        Scanner sc = new Scanner(System.in);
        showTermin();
        System.out.print("Bitte ID von dem gewuenschten Termin eingeben:");
        int id = sc.nextInt();
        System.out.print("-----------\n");
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM user as us\n" +
                "Join usertermin as usev on us.userID = usev.userID\n" +
                "Where usev.terminID = " + id);
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
            showTermin();
            System.out.print("Bitte ID von dem gewuenschten Termin eingeben:");
            int eid = sc.nextInt();
            usertermin(uid, eid);
            check = false;
        }   
    }
}