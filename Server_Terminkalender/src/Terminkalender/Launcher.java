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
    // Liste mit Benutzer + SitzungID
    private LinkedList<Sitzung> aktiveSitzungen;
    int sitzungscounter;
    
    public Launcher(){
        aktiveSitzungen = new LinkedList<>();
        ladeBenutzerliste();
        sitzungscounter = 1;
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
    
    @Override
    public void resetPassword(String username) throws BenutzerException{
        benutzerliste.getBenutzer(username).resetPasswort();
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
     */
    @Override
    public void addTermin(Termin termin, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().addTermin(termin);
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
     */
    @Override
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().addTermin(datum, beginn, ende, titel, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * entfernt den termin mit angegebener id
     * 
     * @param id
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     */
    @Override
    public void removeTermin(int id, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
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
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTermindatum(int id, Datum neuesDatum, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setDatum(neuesDatum, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerBeginn
     * @param sitzungsID
     * @throws BenutzerException
     * @throws TerminException 
     */
    @Override
    public void changeTerminbeginn(int id, Zeit neuerBeginn, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setBeginn(neuerBeginn, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuesEnde
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminende(int id, Zeit neuesEnde, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setEnde(neuesEnde, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neueNotiz
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminnotiz(int id, String neueNotiz, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setNotiz(neueNotiz, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerTitel
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTermintitel(int id, String neuerTitel, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setTitel(neuerTitel, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param neuerOrt
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws TerminException 
     */
    @Override
    public void changeTerminort(int id, String neuerOrt, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setOrt(neuerOrt, eingeloggterBenutzer.getUsername());
    }
    
    /**
     * 
     * @param id
     * @param username
     * @param sitzungsID
     * @throws BenutzerException 
     * @throws Terminkalender.TerminException 
     */
    @Override
    public void addTerminteilnehmer(int id, String username, int sitzungsID) throws BenutzerException, TerminException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
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
     * @param sitzungsID
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAnnehmen(int id, int sitzungsID) throws TerminException, BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
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
     * @param sitzungsID
     * @throws TerminException
     * @throws BenutzerException 
     */
    @Override
    public void terminAblehnen(int id, int sitzungsID) throws TerminException, BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
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
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void changePasswort(String altesPW, String neuesPW, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(!eingeloggterBenutzer.istPasswort(altesPW)){
            throw new BenutzerException("altes Passwort war falsch!");
        }
        eingeloggterBenutzer.setPasswort(neuesPW);
    }
    
    /**
     * 
     * @param neuerVorname
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void changeVorname(String neuerVorname, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setVorname(neuerVorname);
    }
    
    /**
     * 
     * @param neuerNachname
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void changeNachname(String neuerNachname, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setNachname(neuerNachname);
    }
    
    /**
     * 
     * @param neueEmail
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void changeEmail(String neueEmail, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.setEmail(neueEmail);
    }
    
    /**
     * 
     * @param username 
     * @param sitzungsID 
     * @throws Terminkalender.BenutzerException 
     */
    @Override
    public void addKontakt(String username, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        if(!benutzerliste.existiertBenutzer(username)){
            throw new BenutzerException("Benutzername existiert nicht!");
        }
        eingeloggterBenutzer.addKontakt(username);
    }

    /**
     * 
     * @param username
     * @param sitzungsID
     * @throws BenutzerException 
     */
    @Override
    public void removeKontakt(String username, int sitzungsID) throws BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.removeKontakt(username);
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
     * @param id
     * @param sitzungsID
     * @throws TerminException 
     * @throws Terminkalender.BenutzerException 
     */
    @Override
    public void changeEditierrechte(boolean editierbar, int id, int sitzungsID) throws TerminException, BenutzerException{
        Benutzer eingeloggterBenutzer = istEingeloggt(sitzungsID);
        eingeloggterBenutzer.getTerminkalender().getTerminByID(id).setEditierbar(editierbar, eingeloggterBenutzer.getUsername());
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

    private Benutzer istEingeloggt(int sitzungsID) throws BenutzerException {
        for(Sitzung sitzung : aktiveSitzungen){
            if(sitzung.compareWithSitzungsID(sitzungsID)){
                return sitzung.getEingeloggterBenutzer();
            }
        }
        throw new BenutzerException("ungültige Sitzungs-ID");
    }
}
