/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import Terminkalender.DBHandler;
import Terminkalender.Datum;
import Terminkalender.Zeit;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 *
 * @author Müller_Admin
 */
public class DBTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, Datum.DatumException, Zeit.ZeitException{
        String name = "testuser";
        String password = "password";
        String email = "email";
        DBHandler test = new DBHandler();
        test.displayAuswahl();
        /*for (int i = 0; i < 40; i++){
        test.addUser(name+i, password, email, i, i);
        }*/

        //test.addUser("name", password, email, 2, 2);  FUNKTIONIERT
        //test.resetPassword("Dingens", "123");         FUNKTIONIERT
        //test.addKontakt(2, 1);                        FUNKTIONIERT
        //test.removeKontakt(2, 1);                     FUNKTIONIERT
        //test.changePasswort("123", 0);                FUNKTIONIERT
        //test.changeVorname("Jan", 0);                 FUNKTIONIERT
        //test.changeNachname("Steffes", 0);            FUNKTIONIERT
        //test.changeEmail("mailussu", 0);              FUNKTIONIERT
        //test.addnewTermin(new Datum(13,6,1993), new Zeit(12,30), new Zeit(18,0), "Geburtstag", 0, 0);     FUNKTIONIERT
    }
}
