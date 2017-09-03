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
     * @param TerminID
     * @return
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     */
    @Override
    public Termin getTermin(int TerminID) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getTerminkalender().getTerminByID(TerminID);
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
     * @throws Terminkalender.TerminException 
     */
    @Override
    public void removeTermin(int id) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        if(eingeloggterBenutzer.getUsername().equals(eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getOwner())){
            for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTeilnehmerliste()){      
                if(!teilnehmer.getUsername().equals(eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getOwner())){                                
                    benutzerliste.getBenutzer(teilnehmer.getUsername()).getTerminkalender().removeTerminByID(id);
                    benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(
                            eingeloggterBenutzer.getUsername() 
                            + " hat den Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getDatum().toString()
                            + " gelöscht");   
                }            
            }
        }
        else{
            for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTeilnehmerliste()){
                if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                    benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(
                            eingeloggterBenutzer.getUsername() 
                            + " nimmt nicht mehr an dem Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getDatum().toString()
                            + " teil"); 
                }
            }
            try {
                eingeloggterBenutzer.getTerminkalender().getTerminByID(id).removeTeilnehmer(eingeloggterBenutzer.getUsername());
            } catch (TerminException e) {
                System.out.println(e.getMessage());
            }
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
     * @throws Terminkalender.TerminException 
     */
    @Override
    public void addTerminteilnehmer(int id, String username) throws BenutzerException, TerminException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzer: " + username + " exisitert nicht!");
        }
        benutzerliste.getBenutzer(username).addAnfrage(eingeloggterBenutzer.getTerminkalender().getTerminByID(id), eingeloggterBenutzer.getUsername());
        benutzerliste.getBenutzer(username).addTermin(eingeloggterBenutzer.getTerminkalender().getTerminByID(id));
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).addTeilnehmer(username);
    }
    
    /**
     * 
     * @param id
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAnnehmen(int id) throws TerminException, BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        benutzerliste.getBenutzer(eingeloggterBenutzer.getUsername()).getTerminkalender().getTerminByID(id).changeTeilnehmerNimmtTeil(eingeloggterBenutzer.getUsername());
        for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTeilnehmerliste()){
                if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                    benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(
                            eingeloggterBenutzer.getUsername() 
                            + " nimmt an dem Termin '" 
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTitel()
                            + "' am "
                            + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getDatum().toString()
                            + " teil"); 
                }
            }
    }
    
    
    /**
     * 
     * @param id
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAblehnen(int id) throws TerminException, BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        for(Teilnehmer teilnehmer : eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTeilnehmerliste()){
            if(!eingeloggterBenutzer.getUsername().equals(teilnehmer.getUsername())){ 
                benutzerliste.getBenutzer(teilnehmer.getUsername()).addMeldung(
                        eingeloggterBenutzer.getUsername() 
                        + " hat den Termin '" 
                        + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getTitel()
                        + "' am "
                        + eingeloggterBenutzer.getTerminkalender().getTerminByID(id).getDatum().toString()
                        + " abgelehnt"); 
            }
        }
        try {
            eingeloggterBenutzer.getTerminkalender().getTerminByID(id).removeTeilnehmer(eingeloggterBenutzer.getUsername());
        } catch (TerminException e) {
            System.out.println(e.getMessage());
        } 
        eingeloggterBenutzer.getTerminkalender().removeTerminByID(id);
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
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzername existiert nicht!");
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
     */
    @Override
    public LinkedList<Termin> getTermineInKalenderwoche(int kalenderwoche, int jahr) throws BenutzerException{
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
    public LinkedList<Termin> getTermineInMonat(int monat, int jahr) throws BenutzerException, TerminException{
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
     * 
     * @return
     * @throws BenutzerException 
     */
    @Override
    public LinkedList<Meldungen> getMeldungen() throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        return eingeloggterBenutzer.getMeldungen();
    }
    
    /**
     * 
     * @param index
     * @throws BenutzerException 
     */
    @Override
    public void deleteMeldung(int index) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
        eingeloggterBenutzer.deleteMeldung(index);
    }
    
    /**
     * 
     * @param index
     * @throws BenutzerException 
     */
    @Override
    public void setMeldungenGelesen(int index) throws BenutzerException{
        if(!eingeloggt){
            throw new BenutzerException("noch nicht eingeloggt");
        }
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
        try {
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
        
    }
}
