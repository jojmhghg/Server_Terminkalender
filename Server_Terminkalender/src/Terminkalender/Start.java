/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Terminkalender;

/**
 *
 * @author 
 */
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Start {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
        DBHandler daten = new DBHandler();
        daten.displayAuswahl();
    }
}