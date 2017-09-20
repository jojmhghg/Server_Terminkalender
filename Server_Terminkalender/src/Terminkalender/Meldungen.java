/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.io.Serializable;

/**
 *
 * @author timtim
 */
public class Meldungen implements Serializable{
    protected final String text;
    protected boolean gelesen;
    protected int meldungsID;
    
    Meldungen(String text, int meldungsID){
        this.text = text;
        this.gelesen = false;
        this.meldungsID = meldungsID;
    }
    
    public void meldungGelesen(){
        this.gelesen = true;
    }
    
    public String getText(){
        return this.text;
    }
    
    public boolean getStatus(){
        return this.gelesen;
    }
}
