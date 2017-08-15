/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Tim Meyer
 */
public class Terminkalender implements Serializable{

    private final LinkedList<Termin> terminkalender;
    private int idCounter;
    
    Terminkalender(){
        terminkalender = new LinkedList<>();
        idCounter = 1;
    }
       

    /**
     * 
     * @param id
     * @return 
     */
    public Termin getTerminByID(int id){
        //TODO: suche in terminkalender nach dem Termin mit id = id, falls gefunden gebe ihn zurück, sonst werfe fehler
        return terminkalender.getFirst(); //muss angepasst werden
    }
    
    /**
     * 
     * @param datum
     * @param beginn
     * @param ende
     * @param titel
     * @param username
     * @throws TerminException 
     */
    public void addTermin(Datum datum, Zeit beginn, Zeit ende, String titel, String username) throws TerminException{
        terminkalender.add(new Termin(datum, beginn, ende, titel, idCounter, username));
        idCounter++;
    }
    
    /**
     * Hilfsmethode für getTerminImMonat und getTerminInWoche
     * 
     * @param termin 
     */
    public void addTermin(Termin termin){
        terminkalender.add(termin);
    }
    
    /**
     * gibt alle Termine im Monat 'monat' in LinkedList zurück
     * 
     * @param monat
     * @param jahr
     * @return 
     * @throws Server.TerminException 
     */
    public Terminkalender getTermineImMonat(Monat monat, int jahr) throws TerminException{
        Terminkalender monatsauszug = new Terminkalender();
        
        for(Termin termin : terminkalender){
            if(termin.getDatum().getMonat() == monat.getWert() && termin.getDatum().getJahr() == jahr){
                monatsauszug.addTermin(termin);
            }
        }
        
        return monatsauszug;
    }
    
    /**
     * gibt alle Termine der übergebenen Kalenderwoche in LinkedList zurück
     * 
     * @param kalenderwoche
     * @param jahr
     * @return 
     * @throws Server.TerminException 
     */
    public Terminkalender getTermineInWoche(int kalenderwoche, int jahr) throws TerminException{
        Terminkalender wochenauszug = new Terminkalender();
        
        for(Termin termin : terminkalender){
            if(termin.getDatum().getKalenderwoche() == kalenderwoche && termin.getDatum().getJahr() == jahr){
                wochenauszug.addTermin(termin);
            }
        }
        
        return wochenauszug;
    }

    /**
     * 
     * @param id 
     */
    public void removeTerminByID(int id){
        terminkalender.remove(getTerminByID(id));
    }
    
}