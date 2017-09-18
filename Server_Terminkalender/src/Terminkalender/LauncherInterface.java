package Terminkalender;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
import java.sql.SQLException;
import java.util.LinkedList;


/**
 *
 * @author Tim Meyer
 */
public interface LauncherInterface extends Remote{
    /* initiale Methoden */
    public void createUser(String username, String passwort, String email) throws RemoteException, BenutzerException, SQLException;
    public int einloggen(String username, String passwort) throws RemoteException, BenutzerException;
    public void ausloggen(int sitzungsID) throws RemoteException;
    public void resetPassword(String username) throws RemoteException, BenutzerException, SQLException;
    
    /* alles zu der Kontaktliste */
    public void addKontakt(String username, int sitzungsID) throws RemoteException, BenutzerException;
    public void removeKontakt(String username, int sitzungsID) throws BenutzerException, RemoteException;
    public LinkedList<String> getKontakte(int sitzungsID) throws BenutzerException, RemoteException;
    
    /* alles zu den Benutzerdaten */
    public void changePasswort(String altesPW, String neuesPW, int sitzungsID) throws RemoteException, BenutzerException;
    public void changeVorname(String neuerVorname, int sitzungsID) throws RemoteException, BenutzerException;
    public void changeNachname(String neuerNachname, int sitzungsID) throws RemoteException, BenutzerException;
    public void changeEmail(String neueEmail, int sitzungsID) throws RemoteException, BenutzerException;
    public String getUsername(int sitzungsID) throws RemoteException, BenutzerException;
    public String getVorname(int sitzungsID) throws RemoteException, BenutzerException;
    public String getNachname(int sitzungsID) throws RemoteException, BenutzerException;
    public String getEmail(int sitzungsID) throws RemoteException, BenutzerException;
    
    /* alles zu Terminen */
    public Termin getTermin(int TerminID, int sitzungsID) throws RemoteException, BenutzerException, TerminException;
    public void addTermin(Termin termin, int sitzungsID) throws RemoteException, BenutzerException, TerminException; /* notwendig? */
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel, int sitzungsID) throws RemoteException, BenutzerException, TerminException;
    public void removeTermin(int id, int sitzungsID) throws RemoteException, BenutzerException, TerminException;
    public void changeEditierrechte(boolean editierbar, int id, int sitzungsID) throws TerminException, BenutzerException, RemoteException;
    public void changeTerminort(int id, String neuerOrt, int sitzungsID) throws BenutzerException, RemoteException, TerminException;
    public void changeTermintitel(int id, String neuerTitel, int sitzungsID) throws BenutzerException, RemoteException, TerminException;
    public void changeTerminnotiz(int id, String neueNotiz, int sitzungsID) throws BenutzerException, RemoteException, TerminException;
    public void changeTerminende(int id, Zeit neuesEnde, int sitzungsID) throws BenutzerException, TerminException, RemoteException;
    public void changeTerminbeginn(int id, Zeit neuerBeginn, int sitzungsID) throws BenutzerException, TerminException, RemoteException;  
    public void changeTermindatum(int id, Datum neuesDatum, int sitzungsID) throws BenutzerException, RemoteException, TerminException;
    public void addTerminteilnehmer(int id, String username, int sitzungsID) throws RemoteException, BenutzerException, TerminException;
    public LinkedList<Termin> getTermineInKalenderwoche(int kalenderwoche, int jahr, int sitzungsID) throws RemoteException, BenutzerException;
    public LinkedList<Termin> getTermineInMonat(int monat, int jahr, int sitzungsID) throws RemoteException, TerminException, BenutzerException;
    public LinkedList<Termin> getTermineAmTag(Datum datum, int sitzungsID) throws RemoteException, TerminException, BenutzerException;
    public void terminAnnehmen(int id, int sitzungsID) throws RemoteException, TerminException, BenutzerException;
    public void terminAblehnen(int id, int sitzungsID) throws RemoteException, TerminException, BenutzerException;
    
    /* alles zu ausstehenden Meldungen */ 
    public LinkedList<Meldungen> getMeldungen(int sitzungsID) throws RemoteException, BenutzerException;
    public void deleteMeldung(int index, int sitzungsID) throws RemoteException, BenutzerException;
    public void setMeldungenGelesen(int index, int sitzungsID) throws BenutzerException, RemoteException;
}

