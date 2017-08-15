/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.io.Serializable;

/**
 *
 * @author Tim Meyer
 */
class Anfrage implements Serializable{
    private final Termin termin;
    private final String absender;
    
    Anfrage(Termin termin, String absender){
        this.termin = termin;
        this.absender = absender;
    }
    
    public Termin getTermin(){
        return termin;
    }
    
    public String getAbsender(){
        return absender;
    }
}
