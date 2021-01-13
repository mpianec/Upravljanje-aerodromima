package org.foi.nwtis.mpianec.web.podaci;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import org.foi.nwtis.mpianec.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matija
 */
public class Baza {
    static ServletContext sc;
    static BP_Konfiguracija konf;
    
    private Baza() {
    }
    
    public static Baza getInstance() {
        return BazaHolder.INSTANCE;
    }
    
    private static class BazaHolder {
        ServletContext sc;
        private static final Baza INSTANCE = new Baza();
    }
    /**
     * Metoda stvara konekciju na bazu podataka
     * @return vraća konekciju
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static Connection dajBazu() throws ClassNotFoundException, SQLException{        
        sc = Slusac.getSc();
        konf = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        String url = konf.getServerDatabase() + konf.getUserDatabase();
        String korisnik = konf.getUserUsername();
        String lozinka = konf.getUserPassword();
        Class.forName(konf.getDriverDatabase());
        Connection con = DriverManager.getConnection(url, korisnik, lozinka);
        return con;
    }
    
}
