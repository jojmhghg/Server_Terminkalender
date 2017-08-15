package Terminkalender;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
import java.util.LinkedList;


/**
 *
 * @author Tim Meyer
 */
public interface LauncherInterface extends Remote{
    /* initiale Methoden */
    public void createUser(String username, String passwort, String email) throws RemoteException, BenutzerException;
    public boolean einloggen(String username, String passwort) throws RemoteException;
    public void ausloggen() throws RemoteException;
    
    /* alles zu der Kontaktliste */
    public void addKontakt(String username) throws RemoteException, BenutzerException;
    public void removeKontakt(String username) throws BenutzerException, RemoteException;
    public LinkedList<String> getKontakte() throws BenutzerException, RemoteException;
    
    /* alles zu den Benutzerdaten */
    public void changePasswort(String altesPW, String neuesPW) throws RemoteException, BenutzerException;
    public void changeVorname(String neuerVorname) throws RemoteException, BenutzerException;
    public void changeNachname(String neuerNachname) throws RemoteException, BenutzerException;
    public void changeEmail(String neueEmail) throws RemoteException, BenutzerException;
    public String getUsername() throws RemoteException, BenutzerException;
    public String getVorname() throws RemoteException, BenutzerException;
    public String getNachname() throws RemoteException, BenutzerException;
    public String getEmail() throws RemoteException, BenutzerException;
    
    /* alles zu Terminen */
    public void addTermin(Termin termin) throws RemoteException, BenutzerException, TerminException; /* notwendig? */
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel) throws RemoteException, BenutzerException, TerminException;
    public void removeTermin(int id) throws RemoteException, BenutzerException;
    public void changeEditierrechte(boolean editierbar, int id) throws TerminException, BenutzerException, RemoteException;
    public void changeTerminort(int id, String neuerOrt) throws BenutzerException, RemoteException, TerminException;
    public void changeTermintitel(int id, String neuerTitel) throws BenutzerException, RemoteException, TerminException;
    public void changeTerminnotiz(int id, String neueNotiz) throws BenutzerException, RemoteException, TerminException;
    public void changeTerminende(int id, Zeit neuesEnde) throws BenutzerException, TerminException, RemoteException;
    public void changeTerminbeginn(int id, Zeit neuerBeginn) throws BenutzerException, TerminException, RemoteException;  
    public void changeTermindatum(int id, Datum neuesDatum) throws BenutzerException, RemoteException, TerminException;
    public void addTerminteilnehmer(int id, String username) throws RemoteException, BenutzerException;
    public Terminkalender getTermineInKalenderwoche(int kalenderwoche, int jahr) throws RemoteException, TerminException, BenutzerException;
    public Terminkalender getTermineInMonat(Monat monat, int jahr) throws RemoteException, TerminException, BenutzerException;
    public void terminAnnehmen(String username, int id, Termin termin) throws RemoteException, TerminException, BenutzerException;
    public void terminAblehnen(String username, int id, Termin termin) throws RemoteException, TerminException, BenutzerException;
    
    /* alles zu offenen Anfragen */
    
    /* alles zu ausstehenden Meldungen */ 
}

