/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

/**
 *
 * @author TimMeyer
 */
class Sitzung {
    
    private final Benutzer eingeloggterBenutzer;
    private final int sitzungsID;
    
    Sitzung(Benutzer benutzer, int sitzungID){
        this.eingeloggterBenutzer = benutzer;
        this.sitzungsID = sitzungID;
    }
    
    public boolean compareWithSitzungsID(int value){
        return sitzungsID == value;
    }
    
    public Benutzer getEingeloggterBenutzer(){
        return eingeloggterBenutzer;
    }
}
