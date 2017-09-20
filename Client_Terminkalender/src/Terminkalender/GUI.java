/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import Terminkalender.GUIPart.Fenster;
import Terminkalender.GUIPart.Hauptfenster;
import Terminkalender.GUIPart.Registrieren;
import java.io.InputStream;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;

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
    
    /**
     * Methode die GUI startet
     * 
     */
    public void startGUI(){
        try{
            anmeldenGUI();
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
    
    
    /**
     * GUI zum Startbildschirm
     * 
     * @throws RemoteException
     * @throws BenutzerException
     * @throws TerminException
     * @throws Terminkalender.Datum.DatumException
     * @throws Terminkalender.Zeit.ZeitException 
     */
    private void anmeldenGUI() throws RemoteException, BenutzerException, TerminException, Datum.DatumException, Zeit.ZeitException{ 
        Fenster start = new Fenster(stub);
        start.setVisible(true);
        
    }
    
}
