/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Tim Meyer
 */
public class Server {
    
    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.rmi.AlreadyBoundException
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Launcher launcher = new Launcher();
        
        LauncherInterface stub = (LauncherInterface)UnicastRemoteObject.exportObject(launcher, 0);

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Terminkalender", stub);
            
        System.out.println("Server lÃ¤uft!");

    }      
    
}
