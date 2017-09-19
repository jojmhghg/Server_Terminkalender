/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import Terminkalender.GUIPart.Fenster;
import Terminkalender.GUIPart.Hauptfenster;
import java.io.InputStream;
import java.rmi.RemoteException;

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
    
    public void startGUI(){
        try{
            guibildschirm();
	}
        
	catch (RemoteException e){
            System.err.println(e.getMessage());
	}
        
        catch (BenutzerException e) {
            System.err.println(e.getMessage());
        }
        
        catch (TerminException e) {
            System.err.println(e.getMessage());
        } 
        
        catch (Datum.DatumException e) {
            System.err.println(e.getMessage());
        } 
        
        catch (Zeit.ZeitException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void guibildschirm() throws RemoteException, BenutzerException, TerminException, Datum.DatumException, Zeit.ZeitException{ 
        Fenster start = new Fenster();
        start.setVisible(true);
        
        String username;
        String password;
        
        username = start.getTexte();
        password = start.getTexte();
        
        if(stub.einloggen(username, password)) {
            Hauptfenster start2 = new Hauptfenster();
        }
        
        else {
            
        }
        
    }
    
}
