/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mpianec.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mpianec.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mpianec.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mpianec.web.dretve.PreuzimanjeAviona;
import org.foi.nwtis.mpianec.web.dretve.ServerDretva;


/**
 * Web application lifecycle listener.
 *
 * @author nwtis_1
 */
@WebListener
public class Slusac implements ServletContextListener {

    private static ServletContext sc;
    PreuzimanjeAviona preuzimanjeAviona;
    ServerDretva server;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sc = sce.getServletContext();
        String putanja = sc.getRealPath("/WEB-INF");
        String datoteka = putanja + File.separator + sc.getInitParameter("konfiguracija");
        try {
            BP_Konfiguracija bpk = new BP_Konfiguracija(datoteka);
            sc.setAttribute("BP_Konfig", bpk);
            Konfiguracija konf=KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            sc.setAttribute("Konfig", konf);
            System.out.println("Učitana konfiguracija");
            preuzimanjeAviona = new PreuzimanjeAviona();
            preuzimanjeAviona.start();
            server=new ServerDretva();
            server.start();
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(Slusac.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (preuzimanjeAviona != null) {
            preuzimanjeAviona.interrupt();
        }
        sc = sce.getServletContext();
        sc.removeAttribute("BP_Konfig");
    }

    public static ServletContext getSc() {
        return sc;
    }
}
