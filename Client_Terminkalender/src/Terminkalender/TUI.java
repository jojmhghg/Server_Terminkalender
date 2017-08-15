/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

import Terminkalender.Datum.DatumException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 *
 * @author Tim Meyer
 */
public class TUI {
    LauncherInterface stub;
    
    TUI(LauncherInterface stub){
        this.stub = stub;
    }
        
    public void start(){
    	try{
            startbildschirm();
	} 
	catch (RemoteException e){
            System.err.println(e.getMessage());
	} 
        catch (BenutzerException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void startbildschirm() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        int eingabe;
        boolean wiederholen = true;
            
        do{
	    System.out.println("\n************ Startbildschirm ************\n");
            System.out.println("01 - Anmelden");
            System.out.println("02 - Registieren");
            System.out.println("03 - Passwort vergessen");
            System.out.println("04 - Beenden");	
	    System.out.print("Eingabe: ");
            
	    if(scanner.hasNextInt()){
                eingabe = scanner.nextInt();     
                switch(eingabe){
                    case 1:
                        anmelden();
                        break;
                    case 2:
                        registrieren();
                        break;
                    case 3:
                        System.out.println("\n-----> noch nicht implementiert!");
                        break;
                    case 4:
                        System.out.println("\n-----> Anwendung beendet!");
			wiederholen = false;
                        break;
                    default:    
                        System.out.println("\n-----> Ungueltige Eingabe!");
                        break;
                }
            } 
            else{
                System.out.println("\n-----> Ungueltige Eingabe!");
                scanner.next();
            }     
        } while(wiederholen);
    }
    
    private void anmelden() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        String username, password;
                
        System.out.println("\n************ Anmelden ************\n");
        System.out.println("Benutzername eingeben:");
        username = scanner.nextLine();        
        System.out.println("Passwort eingeben:");
        password = scanner.nextLine();
               
        if(stub.einloggen(username, password)){
            System.out.println("\n-----> Anmelden erfolgreich!");
            hauptbildschirm();
        }
        else{
            System.out.println("\n-----> Anmelden gescheitert!");
        }            
    }
    
    private void registrieren() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        String username, password, email, again;
        boolean wiederholen;
        
        System.out.println("\n************ Registieren ************\n");
	do{
        	System.out.println("Benutzername eingeben:");
        	username = scanner.nextLine(); 
        	System.out.println("Passwort eingeben:");
        	password = scanner.nextLine();
		System.out.println("E-Mail eingeben:");
        	email = scanner.nextLine();
                wiederholen = false;
		
        	try{
			stub.createUser(username, password, email);
			System.out.println("\n-----> Registrierung erfolgreich");
		}
		catch(BenutzerException e){
			System.err.println("\n" + e.getMessage());
			System.out.print("Eingabe wiederholen? (j/n) ");
			again = scanner.nextLine();
			if(again.equals("j") || again.equals("ja") || again.equals("y") || again.equals("yes")){
				wiederholen = true;
			}
			else{
				System.out.print("\n-----> Registrierung abgebrochen");
			}
        	}
	} while(wiederholen);            
    }

    private void hauptbildschirm() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        int eingabe;
        boolean wiederholen = true;
                      
        do{
	    System.out.println("\n************ Hauptbildschirm ************\n");
            System.out.println("01 - Terminkalender");
            System.out.println("02 - Kontakte");
            System.out.println("03 - Benachrichtigungen");
            System.out.println("04 - Profil");	
            System.out.println("05 - Ausloggen");
	    System.out.print("Eingabe: ");
            
	    if(scanner.hasNextInt()){
                eingabe = scanner.nextInt();     
                switch(eingabe){
                    case 1:
                        terminkalender();
                        break;
                    case 2:
                        kontakte();
                        break;
                    case 3:
                        System.out.println("\n-----> noch nicht implementiert!");
                        break;
                    case 4:
                        profil();
                        break;
                    case 5:
                        stub.ausloggen();
                        System.out.println("\n-----> Ausloggen erfolgreich!");
                        wiederholen = false;
                        break;
                    default:    
                        System.out.println("\n-----> Ungueltige Eingabe!");
                        break;
                }
            } 
            else{
                System.out.println("\n-----> Ungueltige Eingabe!");
                scanner.next();
            }     
        } while(wiederholen);
    }

    private void profil() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        int eingabe;
        String change;
        boolean wiederholen = true;
                      
        do{
	    System.out.println("\n************ Profil ************\n");
            System.out.println("Benutzername: " + stub.getUsername());
            System.out.println("Vorname: " + stub.getVorname() + "(1)");
            System.out.println("Nachname: " + stub.getNachname() + "(2)");
            System.out.println("E-Mail-Adresse: " + stub.getEmail());
            System.out.println("Passwort ändern (3)");
            System.out.println("Zurück (4)");
	    System.out.print("Eingabe: ");
            
	    if(scanner.hasNextInt()){
                eingabe = scanner.nextInt();
                switch(eingabe){
                    case 1:
                        changeVorname();
                        break;
                    case 2:
                        changeNachname();
                        break;
                    case 3:
                        changePW();
                        break;
                    case 4:
                        wiederholen = false;
                        break;
                    default:    
                        System.out.println("\n-----> Ungueltige Eingabe!");
                        break;
                }
            } 
            else{
                System.out.println("\n-----> Ungueltige Eingabe!");
                scanner.next();
            }     
        } while(wiederholen);
    }

    private void changeVorname() throws RemoteException, BenutzerException {
        Scanner scanner = new Scanner(System.in);
        String newName;

        System.out.print("Neuer Vorname: ");
        newName = scanner.nextLine();
        stub.changeVorname(newName);
        System.out.println("\n-----> Vorname erfolgreich geändert!");
    }

    private void changeNachname() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        String newName;

        System.out.print("Neuer Nachname: ");
        newName = scanner.nextLine();
        stub.changeNachname(newName);
        System.out.println("\n-----> Nachname erfolgreich geändert!");
    }

    private void changePW() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        String altesPW, neuesPW;

        System.out.print("Altes Passwort: ");
        altesPW = scanner.nextLine();
        System.out.print("Neues Passwort: ");
        neuesPW = scanner.nextLine();
        try {
            stub.changePasswort(altesPW, neuesPW);
            System.out.println("\n-----> Passwort ändern erfolgreich!");
        } catch (BenutzerException e) {
            System.out.println("\n-----> " + e.getMessage());
        }
    }

    private void kontakte() throws RemoteException, BenutzerException {
        Scanner scanner = new Scanner(System.in);
        int eingabe;
        boolean wiederholen = true;
                      
        do{
	    System.out.println("\n************ Kontakte ************\n");
            System.out.println("01 - Kontakte anzeigen");
            System.out.println("02 - Kontakt hinzufügen");
            System.out.println("03 - Kontakt löschen");
            System.out.println("04 - Zurück");	
	    System.out.print("Eingabe: ");
            
	    if(scanner.hasNextInt()){
                eingabe = scanner.nextInt();     
                switch(eingabe){
                    case 1:
                        showKontakte();
                        break;
                    case 2:
                        addKontakt();
                        break;
                    case 3:
                        removeKontakt();
                        break;
                    case 4:
                        wiederholen = false;
                        break;
                    default:    
                        System.out.println("\n-----> Ungueltige Eingabe!");
                        break;
                }
            } 
            else{
                System.out.println("\n-----> Ungueltige Eingabe!");
                scanner.next();
            }     
        } while(wiederholen);
    }

    private void addKontakt() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        String username;

        System.out.print("Username des Kontakts: ");
        username = scanner.nextLine();
        try {
            stub.addKontakt(username);
            System.out.println("\n-----> Kontakt erfolgreich hinzugefügt!");
        } catch (BenutzerException e) {
            System.out.println("\n-----> " + e.getMessage());
        }
    }

    private void removeKontakt() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        String username;

        System.out.print("Username des Kontakts: ");
        username = scanner.nextLine();
        try {
            stub.removeKontakt(username);
            System.out.println("\n-----> Kontakt erfolgreich gelöscht!");
        } catch (BenutzerException e) {
            System.out.println("\n-----> " + e.getMessage());
        }
    }

    private void showKontakte() throws RemoteException, BenutzerException {
        System.out.println("\n-----> Deine Kontakte(" + stub.getKontakte().size() + "):");
        for(String kontakt : stub.getKontakte()) {
            System.out.println(kontakt);
        }
    }

    private void terminkalender() throws RemoteException, BenutzerException {
        Scanner scanner = new Scanner(System.in);
        int eingabe;
        boolean wiederholen = true;
                      
        do{
	    System.out.println("\n************ Terminkalender ************\n");
            System.out.println("01 - Wochenansicht");
            System.out.println("02 - Montasansicht");
            System.out.println("03 - neuer Termin anlegen");
            System.out.println("04 - Zurück");	
	    System.out.print("Eingabe: ");
            
	    if(scanner.hasNextInt()){
                eingabe = scanner.nextInt();     
                switch(eingabe){
                    case 1:
                        wochenansicht();
                        break;
                    case 2:
                        System.out.println("\n-----> noch nicht implementiert!");
                        break;
                    case 3:
                        terminAnlegen();
                        break;
                    case 4:
                        wiederholen = false;
                        break;
                    default:    
                        System.out.println("\n-----> Ungueltige Eingabe!");
                        break;
                }
            } 
            else{
                System.out.println("\n-----> Ungueltige Eingabe!");
                scanner.next();
            }     
        } while(wiederholen);
    }

    private void terminAnlegen() throws RemoteException, BenutzerException{
        Scanner scanner = new Scanner(System.in);
        Datum datum;
        Zeit start, ende;
        String titel;
        int tag = 0, monat = 0, jahr = 0, stunde = 0, minute = 0;
        boolean nochmal = true;
        
        System.out.print("Titel: ");
        titel = scanner.nextLine();
        
        try{
            do{
                System.out.print("Tag(1-31):");
                if(scanner.hasNextInt()){
                    tag = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal);
            do{
                System.out.print("Monat(1-12):");
                if(scanner.hasNextInt()){
                    monat = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal);
            do{
                System.out.print("Jahr:");
                if(scanner.hasNextInt()){
                    jahr = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal);      
            datum = new Datum(tag, monat, jahr);
            
            do{
                System.out.print("Start-Stunde:");
                if(scanner.hasNextInt()){
                    stunde = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal); 
            do{
                System.out.print("Start-Minute:");
                if(scanner.hasNextInt()){
                    minute = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal); 
            start = new Zeit(stunde, minute);
            
            do{
                System.out.print("End-Stunde:");
                if(scanner.hasNextInt()){
                    stunde = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal); 
            do{
                System.out.print("End-Minute:");
                if(scanner.hasNextInt()){
                    minute = scanner.nextInt();
                    nochmal = false;
                }
                else{
                    scanner.next();
                }  
            } while(nochmal); 
            ende = new Zeit(stunde, minute);

            stub.addTermin(datum, start, ende, titel);
        }
        catch(DatumException | Zeit.ZeitException | TerminException e){
            System.out.println("\n-----> " + e.getMessage());
        }
            
        
        
    }

    private void wochenansicht() throws RemoteException {
        stub.getTermineInKalenderwoche(0, 0);
        
    }
    
}
