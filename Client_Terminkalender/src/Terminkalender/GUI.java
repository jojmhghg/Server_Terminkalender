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
        Fenster start = new Fenster();
        start.setVisible(true);
        
        String username;
        String password;
        
        username = start.getUsername();
        password = start.getPassword();
        
        if(stub.einloggen(username, password)) {
            
        }
        
        else {
            System.out.println("\n-----> Anmelden gescheitert!");
        }
        
    }
    
    /**
     * GUI zum Registrieren
     * 
     * 
     */
    private void registrierenGUI() throws RemoteException {
        Registrieren registrieren = new Registrieren();
        registrieren.setVisible(true);
        
        String username, password, email, again;
        boolean wiederholen = false;
        
        do {
            username = registrieren.getRegUsername();
            password = registrieren.getRegUsername();
            email = registrieren.getRegEmail();

            try{
                stub.createUser(username, password, email);
            }
            catch(BenutzerException e){
                JOptionPane.showInputDialog(e.getMessage());
            int input = JOptionPane.showOptionDialog(null, "Eingabe wiederholen?", "Registrieren", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            if(input == JOptionPane.OK_OPTION)
                wiederholen = true;
            
            else{
                
            }
                JOptionPane.showInputDialog("Registrierung abgebrochen");
            }
            
            
        }while(wiederholen);
    }
}
