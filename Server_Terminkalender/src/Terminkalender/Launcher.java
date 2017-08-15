/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.util.LinkedList;

/**
 *
 * @author Tim Meyer
 */
public class Launcher implements LauncherInterface{
    
    private BenutzerListe benutzerliste;
    private Benutzer eingeloggterBenutzer;
    private boolean eingeloggt;
    
    public Launcher(){
        ladeBenutzerliste();
        eingeloggt = false;
    }

    /**
     * 
     */
    @Override
    public void ausloggen(){
        eingeloggt = false;
        eingeloggterBenutzer = null;
    }
    
    /**
     * 
     * @param username
     * @param passwort
     * @param email
     * @throws BenutzerException 
     */
    @Override
    public void createUser(String username, String passwort, String email) throws BenutzerException{
        benutzerliste.addBenutzer(username, passwort, email);
    }
    
    /**
     * gibt Benutzer zu übergebenen username zurück oder wirft Fehler falls
     * dieser nicht vorhanden ist
     * 
     * @param username
     * @param passwort 
     * @return  
     */
    @Override
    public boolean einloggen(String username, String passwort){
        try {
            if(benutzerliste.getBenutzer(username).istPasswort(passwort)){
                eingeloggterBenutzer = benutzerliste.getBenutzer(username);
                eingeloggt = true;
                return true;
            }
            else{
                return false;
            }
        } catch (BenutzerException e) {
            return false;
        }
    }
    
    /**
     * 
     * @param termin
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public void addTermin(Termin termin) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().addTermin(termin);
    }
    
    /**
     * fügt dem eingeloggten Benutzer den Termin mit den übergebenen Parametern hinzu
     * 
     * @param datum
     * @param beginn
     * @param ende
     * @param titel
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().addTermin(datum, beginn, ende, titel, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * entfernt den termin mit angegebener id
     * 
     * @param id
     * @throws BenutzerException 
     */
    @Override
    public void removeTermin(int id) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().removeTerminByID(id);
    }
    
    /**
     * 
     * @param id
     * @param neuesDatum
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTermindatum(int id, Datum neuesDatum) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setDatum(neuesDatum, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerBeginn
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public void changeTerminbeginn(int id, Zeit neuerBeginn) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setBeginn(neuerBeginn, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuesEnde
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminende(int id, Zeit neuesEnde) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setEnde(neuesEnde, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neueNotiz
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminnotiz(int id, String neueNotiz) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setNotiz(neueNotiz, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerTitel
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTermintitel(int id, String neuerTitel) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setTitel(neuerTitel, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerOrt
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminort(int id, String neuerOrt) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setOrt(neuerOrt, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param username
     * @throws BenutzerException 
     */
    @Override
    public void addTerminteilnehmer(int id, String username) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzer: " + username + " exisitert nicht!");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).addTeilnehmer(username);
        benutzerliste.getBenutzer(username).addAnfrage(eingeloggterBenutzer.getTerminkalender().getTerminByID(id), username);
    }
    
    /**
     * 
     * @param username
     * @param id
     * @param termin
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAnnehmen(String username, int id, Termin termin) throws TerminException, BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().addTermin(termin);
        benutzerliste.getBenutzer(username).getTerminkalender().getTerminByID(id).changeTeilnehmerNimmtTeil(username);
        //TODO: benutzerliste.getBenutzer(username) eine Meldung schicken, dass eingeloggterBenutzer teilnimmt!
    }
    
    /**
     * 
     * @param username
     * @param id
     * @param termin
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAblehnen(String username, int id, Termin termin) throws TerminException, BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        benutzerliste.getBenutzer(username).getTerminkalender().getTerminByID(id).removeTeilnehmer(username);
        //TODO: benutzerliste.getBenutzer(username) eine Meldung schicken, dass eingeloggterBenutzer nicht teilnimmt!
    }
    
    /**
     * 
     * @param altesPW
     * @param neuesPW
     * @throws BenutzerException 
     */
    @Override
    public void changePasswort(String altesPW, String neuesPW) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        if(!eingeloggterBenutzer.istPasswort(altesPW)){
            throw new BenutzerException("altes Passwort war falsch!");
        }
        eingeloggterBenutzer.setPasswort(neuesPW);
    }
    
    /**
     * 
     * @param neuerVorname
     * @throws BenutzerException 
     */
    @Override
    public void changeVorname(String neuerVorname) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.setVorname(neuerVorname);
    }
    
    /**
     * 
     * @param neuerNachname
     * @throws BenutzerException 
     */
    @Override
    public void changeNachname(String neuerNachname) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.setNachname(neuerNachname);
    }
    
    /**
     * 
     * @param neueEmail
     * @throws BenutzerException 
     */
    @Override
    public void changeEmail(String neueEmail) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.setEmail(neueEmail);
    }
    
    /**
     * 
     * @param username 
     * @throws Terminkalender.BenutzerException 
     */
    @Override
    public void addKontakt(String username) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.addKontakt(username);
    }

    /**
     * 
     * @param username
     * @throws BenutzerException 
     */
    @Override
    public void removeKontakt(String username) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.removeKontakt(username);
    }
    
    /**
     *
     * @return
     * @throws BenutzerException
     */
    @Override
    public LinkedList<String> getKontakte() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getKontaktliste();
    }
   
    /**
     * 
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getUsername() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getUsername();
    }
    
    /**
     * 
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getVorname() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getVorname();
    }
    
    /**
     * 
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getNachname() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getNachname();
    }
    
    /**
     * 
     * @return
     * @throws BenutzerException 
     */
    @Override
    public String getEmail() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getEmail();
    }
    
    /**
     * 
     * @param kalenderwoche
     * @param jahr
     * @return
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public Terminkalender getTermineInKalenderwoche(int kalenderwoche, int jahr) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getTerminkalender().getTermineInWoche(kalenderwoche, jahr);
    }
    
    /**
     * 
     * @param monat
     * @param jahr
     * @return
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public Terminkalender getTermineInMonat(Monat monat, int jahr) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getTerminkalender().getTermineImMonat(monat, jahr);
    }
    
    /**
     * 
     * @param editierbar
     * @param id
     * @throws TerminException 
     * @throws Terminkalender.BenutzerException 
     */
    @Override
    public void changeEditierrechte(boolean editierbar, int id) throws TerminException, BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setEditierbar(editierbar, eingeloggterBenutzer.getUsername());
    }

    /**
     * Hilfsmethode um Benutzerliste zu füllen
     * 
     */
    private void ladeBenutzerliste() {
        //TODO: hier wird die Benutzerliste mit den Daten aus der DB gefüllt
        benutzerliste = new BenutzerListe();
    }
}
