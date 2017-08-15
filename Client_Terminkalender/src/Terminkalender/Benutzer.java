/*
 * TODO: alles mit todo markiert + Metoden für getTermineImMonat & getTermineInWoche
 * implementieren
 * 
 */
package Terminkalender;

import java.util.LinkedList;
import java.io.Serializable;

/**
 *
 * @author Tim Meyer
 */
public class Benutzer implements Serializable{
    
    private final String username;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private Terminkalender terminkalender;
    private LinkedList<Anfrage> terminanfragen;
    private LinkedList<String> kontaktliste; 
    private LinkedList<String> meldungen;
    
    /**
     * 
     * @param username
     * @param passwort
     * @param email
     * @throws BenutzerException 
     */
    Benutzer(String username, String passwort, String email) throws BenutzerException{
        if(username.length() < 4 || username.length() > 12){
            throw new BenutzerException("Der Username sollte zwischen 4 und 12 Zeichen lang sein");
        }
        if(passwort.length() < 4 || passwort.length() > 12){
            throw new BenutzerException("Das Passwort sollte zwischen 4 und 12 Zeichen lang sein");
        }
        
        this.email = email;
        this.username = username;
        this.passwort = passwort;
        this.nachname = "";
        this.vorname = "";
        this.terminkalender = new Terminkalender();
        this.kontaktliste = new LinkedList<>();
        this.meldungen = new LinkedList<>();
        this.terminanfragen = new LinkedList<>();
    } 
    
    //Getter:
    public String getUsername(){
        return username;
    }
    public String getPasswort(){
        return passwort;
    }
    public String getNachname(){
        return nachname;
    }
    public String getVorname(){
        return vorname;
    }
    public String getEmail(){
        return email;
    } 
    public Terminkalender getTerminkalender(){
        return terminkalender;
    }
    public final LinkedList<String> getKontaktliste(){
        return kontaktliste;
    }
    public LinkedList<String> getMeldungen(){
        return meldungen;
    }
    public LinkedList<Anfrage> getTerminanfragen(){
        return terminanfragen;
    }
    
    //Setter:
    public void setNachname(String nachname){
        this.nachname = nachname;
    }
    public void setVorname(String vorname){
        this.vorname = vorname;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPasswort(String neuesPasswort) throws BenutzerException{
        if(neuesPasswort.length() < 4 || neuesPasswort.length() > 12){
            throw new BenutzerException("Das Passwort sollte zwischen 4 und 12 Zeichen lang sein");
        }
        this.passwort = neuesPasswort;
    }
       
    /**
     * 
     * @param passwort
     * @return 
     */
    public boolean istPasswort(String passwort){
        return (this.passwort.equals(passwort));
    }
    
    /**
     * 
     * @param username 
     * @throws Terminkalender.BenutzerException 
     */
    public void addKontakt(String username) throws BenutzerException{
        for(String kontakt : kontaktliste){
            if(kontakt.equals(username)){
                throw new BenutzerException(username + " bereits in der Kontaktliste vorhanden!");
            }
        }
        kontaktliste.add(username);
    }
    
    /**
     * 
     * @param username 
     * @throws Terminkalender.BenutzerException 
     */
    public void removeKontakt(String username) throws BenutzerException{
        for(String kontakt : kontaktliste){
            if(kontakt.equals(username)){
                throw new BenutzerException(username + " bereits in der Kontaktliste vorhanden!");
            }
        }
        kontaktliste.remove(username);
    }

    /**
     * 
     * @param datum
     * @param beginn
     * @param ende
     * @param titel
     * @throws TerminException 
     */
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel) throws TerminException{
        terminkalender.addTermin(datum, beginn, ende, titel, username);
    }
    
    /**
     * 
     * @param termin
     * @param absender 
     */
    public void addAnfrage(Termin termin, String absender){
        terminanfragen.add(new Anfrage(termin, absender));
    }
    
    /**
     * 
     * @return 
     */
    public Anfrage getNextAnfrage(){
        Anfrage result = terminanfragen.getFirst();
        terminanfragen.removeFirst();
        return result;
    }
}
    

