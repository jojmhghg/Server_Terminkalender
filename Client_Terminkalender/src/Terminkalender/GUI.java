/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import Terminkalender.GUIPart.Fenster;
import java.io.InputStream;

/**
 *
 * @author niroshan
 */
public class GUI {

    /**
     *  GUI
     */
    
    private final LauncherInterface stub;
    private final InputStream inputStream;
    
    GUI(LauncherInterface stub){
        this.stub = stub;
        this.inputStream = System.in;
    }
    
    public static void startGUI() {
        Fenster start = new Fenster();
        start.setVisible(true);
        
        
    }
    
}
