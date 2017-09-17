/*
 * TODO: alles mit todo markiert + Metoden für getTermineImMonat & getTermineInWoche
 * implementieren
 * 
 */
package Terminkalender;

import java.util.LinkedList;
import java.io.Serializable;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Tim Meyer
 */
public class Benutzer implements Serializable{
    
    private int userID;
    private final String username;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private Terminkalender terminkalender;
    private LinkedList<String> kontaktliste; 
    private LinkedList<Meldungen> meldungen;
    
    /**
     * 
     * @param username
     * @param passwort
     * @param email
     * @throws BenutzerException 
     */
    Benutzer(String username, String passwort, String email, int userID) throws BenutzerException{
        if(username.length() < 4 || username.length() > 12){
            throw new BenutzerException("Der Username sollte zwischen 4 und 12 Zeichen lang sein");
        }
        if(passwort.length() < 4 || passwort.length() > 12){
            throw new BenutzerException("Das Passwort sollte zwischen 4 und 12 Zeichen lang sein");
        }
        
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.passwort = passwort;
        this.nachname = "";
        this.vorname = "";
        this.terminkalender = new Terminkalender(userID);
        this.kontaktliste = new LinkedList<>();
        this.meldungen = new LinkedList<>();
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
    public LinkedList<Meldungen> getMeldungen(){
        return meldungen;
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
    
    public void resetPasswort(){
        String newPW = "";
        String to = "abcd@gmail.com";
        String from = "web@gmail.com";
        String host = "localhost";
        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Passwort wurde erfolgreich zurückgesetzt!");
            message.setText("Ihr Passwort wurde erfolgreich zurückgesetzt\nIhr neues Passwort lautet: " + newPW);
            Transport.send(message);
        }
        catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
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
        if(username.equals(this.username)){
            throw new BenutzerException("Du kannst dich nicht selbst hinzufügen!");
        }
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
        boolean inListe = false;
        for(String kontakt : kontaktliste){
            if(kontakt.equals(username)){
                inListe = true;
            }
        }
        if(!inListe){
            throw new BenutzerException(username + " nicht in der Kontaktliste vorhanden!");
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
     * @throws TerminException 
     */
    public void addTermin(Termin termin) throws TerminException{
        terminkalender.addTermin(termin);
    }
    
    /**
     * 
     * @param termin
     * @param absender 
     */
    public void addAnfrage(Termin termin, String absender){
        meldungen.add(new Anfrage(absender + " lädt sie zu einem Termin ein" ,termin, absender));
    }
    
    /**
     *
     * @param meldung
     */
    public void addMeldung(String meldung){
        meldungen.add(new Meldungen(meldung));
    }
    
    /**
     * 
     * @param index 
     */
    public void deleteMeldung(int index){
        meldungen.remove(index);
    }
}
    

