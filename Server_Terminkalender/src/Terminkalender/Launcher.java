/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim Meyer
 */
public class Launcher implements LauncherInterface{
    
    private BenutzerListe benutzerliste;
    // Liste mit Benutzer + SitzungID
    private final LinkedList<Sitzung> aktiveSitzungen;
    private int sitzungscounter;
    private final DBHandler datenbank;
    
    public Launcher(){
        datenbank = new DBHandler(); 
        try {
            datenbank.displayAuswahl();
        } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        aktiveSitzungen = new LinkedList<>();
        sitzungscounter = 1;
        ladeBenutzerliste();
    }

    /**
     * 
     * @param sitzungsID
     */
    @Override
    public void ausloggen(int sitzungsID){
        for(Sitzung sitzung : aktiveSitzungen){
            if(sitzung.compareWithSitzungsID(sitzungsID)){
                aktiveSitzungen.remove(sitzung);
            }
        }
    }
    
    /**
     * 
     * @param username
     * @param passwort
     * @param email
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void createUser(String username, String passwort, String email) throws BenutzerException, SQLException{
        benutzerliste.addBenutzer(username, passwort, email);
        datenbank.addUser(username, passwort, email, benutzerliste.getBenutzer(username).getMeldungsCounter(), benutzerliste.getBenutzer(username).getUserID());
    }
    
    /**
     * gibt Benutzer zu übergebenen username zurück oder wirft Fehler falls
     * dieser nicht vorhanden ist
     * 
     * @param username
     * @param passwort 
     * @return  
     * @throws Terminkalender.BenutzerException  
     */
    @Override
    public int einloggen(String username, String passwort) throws BenutzerException{
        int sitzungsID = 10000000 * sitzungscounter + (int)(Math.random() * 1000000 + 1);
        sitzungscounter++;
        if(benutzerliste.getBenutzer(username).istPasswort(passwort)){
            aktiveSitzungen.add(new Sitzung(benutzerliste.getBenutzer(username), sitzungsID));
            return sitzungsID;
        }
        else{
            return -1;
        }
    }
    
    /**
     * 
     * @param username
     * @throws BenutzerException
     * @throws SQLException 
     */
    @Override
    public void resetPassword(String username) throws BenutzerException, SQLException{
        datenbank.resetPassword(username, benutzerliste.getBenutzer(username).resetPasswort());
    }
    
    /**
     * 
     * @param TerminID
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     */
    @Override
    public Termin getTermin(int TerminID, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID); 
        return eingeloggterBenutzer.getTerminkalender().getTerminByID(TerminID);
    }
    
    
    
    /**
     * 
     * @param termin
     * @param sitzungsID
     * @throws BenutzerException
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void addTermin(Termin termin, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().addTermin(termin);
        datenbank.addTermin(termin.getID(), sitzungsID, 0);
    }
    
    /**
     * fügt dem eingeloggten Benutzer den Termin mit den übergebenen Parametern hinzu
     * 
     * @param datum
     * @param beginn
     * @param ende
     * @param titel
     * @param sitzungsID
     * @throws BenutzerException
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        int terminID = eingeloggterBenutzer.getTerminkalender().addTermin(datum, beginn, ende, titel, eingeloggterBenutzer.getUsername());
        datenbank.addnewTermin(datum, beginn, ende, titel, terminID, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * entfernt den termin mit angegebener id
     * 
     * @param terminID
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void removeTermin(int terminID, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(eingeloggterBenutzer.getUsername().equals(eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getOwner())){
            for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTeilnehmerliste()){      
                if(!teilnehmer.getUsername().equals(eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getOwner())){                                
                    benutzerliste.getBenutzer(teilnehmer.getUsername()).getTerminkalender().removeTerminByID(terminID);
                    String text = eingeloggterBenutzer.getUsername() 
                            + " hat den Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getDatum().toString()
                            + " gelöscht";
                    int meldungsID = benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(text);   
                    datenbank.addMeldung(meldungsID, benutzerliste.getBenutzer(teilnehmer.getUsername()).getUserID(), text);
                }            
            }
            datenbank.deleteTermin(terminID);
        }
        else{
            datenbank.removeTermin(terminID, eingeloggterBenutzer.getUserID());
            for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTeilnehmerliste()){
                if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                    String text = eingeloggterBenutzer.getUsername() 
                            + " hat den Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getDatum().toString()
                            + " gelöscht";
                    int meldungsID = benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(text); 
                    datenbank.addMeldung(meldungsID, benutzerliste.getBenutzer(teilnehmer.getUsername()).getUserID(), text);
                }
            }
            try {
                eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).removeTeilnehmer(eingeloggterBenutzer.getUsername());
            } catch (TerminException e) {
                System.out.println(e.getMessage());
            }
        }  
        eingeloggterBenutzer.getTerminkalender().removeTerminByID(terminID);
    }
    
    /**
     * 
     * @param terminID
     * @param neuesDatum
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTermindatum(int terminID, Datum neuesDatum, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setDatum(neuesDatum, eingeloggterBenutzer.getUsername());
        datenbank.changeTermindatum(terminID, neuesDatum);
    }
    
    /**
     * 
     * @param terminID
     * @param neuerBeginn
     * @param sitzungsID
     * @throws BenutzerException
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTerminbeginn(int terminID, Zeit neuerBeginn, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setBeginn(neuerBeginn, eingeloggterBenutzer.getUsername());
        datenbank.changeTerminbeginn(terminID, neuerBeginn);
    }
    
    /**
     * 
     * @param terminID
     * @param neuesEnde
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTerminende(int terminID, Zeit neuesEnde, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setEnde(neuesEnde, eingeloggterBenutzer.getUsername());
        datenbank.changeTerminende(terminID, neuesEnde);
    }
    
    /**
     * 
     * @param terminID
     * @param neueNotiz
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTerminnotiz(int terminID, String neueNotiz, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setNotiz(neueNotiz, eingeloggterBenutzer.getUsername());
        datenbank.changeTerminnotiz(terminID, neueNotiz);
    }
    
    /**
     * 
     * @param terminID
     * @param neuerTitel
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTermintitel(int terminID, String neuerTitel, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setTitel(neuerTitel, eingeloggterBenutzer.getUsername());
        datenbank.changeTermintitel(terminID, neuerTitel);
    }
    
    /**
     * 
     * @param terminID
     * @param neuerOrt
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeTerminort(int terminID, String neuerOrt, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setOrt(neuerOrt, eingeloggterBenutzer.getUsername());
        datenbank.changeTerminort(terminID, neuerOrt);
    }
    
    /**
     * 
     * @param terminID
     * @param username
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void addTerminteilnehmer(int terminID, String username, int sitzungsID) throws BenutzerException, TerminException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzer: " + username + " exisitert nicht!");
        }
        int anfrageID = benutzerliste.getBenutzer(username).addAnfrage(eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID), eingeloggterBenutzer.getUsername());
        benutzerliste.getBenutzer(username).addTermin(eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID));
        datenbank.addTermin(terminID, benutzerliste.getBenutzer(username).getUserID(), 0);
        datenbank.addAnfrage(anfrageID, benutzerliste.getBenutzer(username).getUserID(), terminID, eingeloggterBenutzer.getUserID());
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).addTeilnehmer(username);
    }
    
    /**
     * 
     * @param terminID
     * @param sitzungsID
     * @throws TerminException
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void terminAnnehmen(int terminID, int sitzungsID) throws TerminException, BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        benutzerliste.getBenutzer(eingeloggterBenutzer.getUsername()).getTerminkalender().getTerminByID(terminID).changeTeilnehmerNimmtTeil(eingeloggterBenutzer.getUsername());
        for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTeilnehmerliste()){
                if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                    String text= eingeloggterBenutzer.getUsername() 
                            + " nimmt an dem Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getDatum().toString()
                            + " teil";
                    int meldungsID = benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(text); 
                    datenbank.addMeldung(meldungsID, benutzerliste.getBenutzer(teilnehmer.getUsername()).getUserID(), text);
                }
            }
        datenbank.nimmtTeil(terminID, eingeloggterBenutzer.getUserID());
    }
    
    
    /**
     * 
     * @param terminID
     * @param sitzungsID
     * @throws TerminException
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void terminAblehnen(int terminID, int sitzungsID) throws TerminException, BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTeilnehmerliste()){
            if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                String text = eingeloggterBenutzer.getUsername() 
                        + " hat den Termin '" 
                        + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getTitel()
                        + "' am "
                        + eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).getDatum().toString()
                        + " abgelehnt";
                int meldungsID = benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(text); 
                datenbank.addMeldung(meldungsID, terminID, text);
            }
        }
        try {
            eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).removeTeilnehmer(eingeloggterBenutzer.getUsername());
        } catch (TerminException e) {
            System.out.println(e.getMessage());
        } 
        eingeloggterBenutzer.getTerminkalender().removeTerminByID(terminID);
        datenbank.removeTermin(terminID, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * 
     * @param altesPW
     * @param neuesPW
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changePasswort(String altesPW, String neuesPW, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(!eingeloggterBenutzer.istPasswort(altesPW)){
            throw new BenutzerException("altes Passwort war falsch!");
        }
        eingeloggterBenutzer.setPasswort(neuesPW);
        datenbank.changePasswort(neuesPW, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * 
     * @param neuerVorname
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeVorname(String neuerVorname, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setVorname(neuerVorname);
        datenbank.changeVorname(neuerVorname, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * 
     * @param neuerNachname
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeNachname(String neuerNachname, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setNachname(neuerNachname);
        datenbank.changeNachname(neuerNachname, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * 
     * @param neueEmail
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeEmail(String neueEmail, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setEmail(neueEmail);
        datenbank.changeEmail(neueEmail, eingeloggterBenutzer.getUserID());
    }
    
    /**
     * 
     * @param username 
     * @param sitzungsID 
     * @throws Terminkalender.BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void addKontakt(String username, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzername existiert nicht!");
        }
        eingeloggterBenutzer.addKontakt(username);
        datenbank.addKontakt(eingeloggterBenutzer.getUserID(), benutzerliste.getBenutzer(username).getUserID());
    }

    /**
     * 
     * @param username
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void removeKontakt(String username, int sitzungsID) throws BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.removeKontakt(username);
        datenbank.removeKontakt(eingeloggterBenutzer.getUserID(), benutzerliste.getBenutzer(username).getUserID());
    }
    
    /**
     *
     * @param sitzungsID
     * @return
     * @throws BenutzerException
     */
    @Override
    public LinkedList<String> getKontakte(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getKontaktliste();
    }
   
    /**
     * 
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getUsername(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getUsername();
    }
    
    /**
     * 
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getVorname(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getVorname();
    }
    
    /**
     * 
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getNachname(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getNachname();
    }
    
    /**
     * 
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getEmail(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getEmail();
    }
    
    /**
     * 
     * @param kalenderwoche
     * @param jahr
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public LinkedList<Termin> getTermineInKalenderwoche(int kalenderwoche, int jahr, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getTerminkalender().getTermineInWoche(kalenderwoche, jahr);
    }
    
    /**
     * 
     * @param datum
     * @param sitzungsID
     * @return
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public LinkedList<Termin> getTermineAmTag(Datum datum, int sitzungsID) throws TerminException, BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getTerminkalender().getTermineAmTag(datum);
    }
    
    /**
     * 
     * @param monat
     * @param jahr
     * @param sitzungsID
     * @return
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public LinkedList<Termin> getTermineInMonat(int monat, int jahr, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getTerminkalender().getTermineImMonat(monat, jahr);
    }
    
    /**
     * 
     * @param editierbar
     * @param terminID
     * @param sitzungsID
     * @throws TerminException 
     * @throws Terminkalender.BenutzerException 
     * @throws java.sql.SQLException 
     */
    @Override
    public void changeEditierrechte(boolean editierbar, int terminID, int sitzungsID) throws TerminException, BenutzerException, SQLException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(terminID).setEditierbar(editierbar, eingeloggterBenutzer.getUsername());
        datenbank.changeEditierrechte(editierbar, terminID);
    }

    /**
     * 
     * @param sitzungsID
     * @return
     * @throws BenutzerException 
     */
    @Override
    public LinkedList<Meldungen> getMeldungen(int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        return eingeloggterBenutzer.getMeldungen();
    }
    
    /**
     * 
     * @param index
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void deleteMeldung(int index, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.deleteMeldung(index);
    }
    
    /**
     * 
     * @param index
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void setMeldungenGelesen(int index, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getMeldungen().get(index).meldungGelesen();
    }
    
    /**
     * Hilfsmethode um Benutzerliste zu füllen
     * 
     */
    private void ladeBenutzerliste() {     
        //TODO: hier wird die Benutzerliste mit den Daten aus der DB gefüllt
        benutzerliste = new BenutzerListe();
        testliste();
    }

    private void testliste(){
        /*try {
            benutzerliste.addBenutzer("timeyer", "test", "timeyer@email.de");
            benutzerliste.addBenutzer("sanja", "test", "sanja@email.de");
            benutzerliste.addBenutzer("marco", "test", "marco@email.de");
            benutzerliste.getBenutzer("timeyer").addTermin(new Datum(3, 9, 2017), new Zeit(20, 30), new Zeit(21, 30), "essen gehen");
            benutzerliste.getBenutzer("timeyer").addTermin(new Datum(4, 9, 2017), new Zeit(20, 30), new Zeit(21, 30), "laufen gehen");
            benutzerliste.getBenutzer("timeyer").addTermin(new Datum(5, 9, 2017), new Zeit(20, 30), new Zeit(21, 30), "reden gehen");
            benutzerliste.getBenutzer("timeyer").addTermin(new Datum(6, 9, 2017), new Zeit(20, 30), new Zeit(21, 30), "scheißen gehen");
        } catch (BenutzerException | Datum.DatumException | Zeit.ZeitException | TerminException e) {
            System.out.println(e.getMessage());
        }
        */
    }

    private Benutzer istEingeloggt(int sitzungsID) throws BenutzerException {
        for(Sitzung sitzung : aktiveSitzungen){
            if(sitzung.compareWithSitzungsID(sitzungsID)){
                return sitzung.getEingeloggterBenutzer();
            }
        }
        throw new BenutzerException("ungültige Sitzungs-ID");
    }
}
