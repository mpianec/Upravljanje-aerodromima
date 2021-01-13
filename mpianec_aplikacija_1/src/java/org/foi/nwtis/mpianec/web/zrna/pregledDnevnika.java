/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.web.podaci.Baza;
import org.foi.nwtis.mpianec.web.podaci.Dnevnik;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;

/**
 *
 * @author Matija
 */

@Named(value = "pregledDnevnika")
@SessionScoped
public class pregledDnevnika implements Serializable {

    private List<Dnevnik> dnevnik=new ArrayList<>();
    private int brojRedaka;
    /**
     * Creates a new instance of pregledDnevnika
     */
    public pregledDnevnika() {
        dnevnik=preuzmiDnevnik();
        brojRedaka=tablicaBrojRedaka();
    }
    
    private List<Dnevnik> preuzmiDnevnik(){
        List<Dnevnik> dnevnik=new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            String upit = "Select * from dnevnik";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Dnevnik d = new Dnevnik();
                d.setId(rs.getInt("id"));
                d.setOpis(rs.getString("opis"));
                d.setTipLoga(rs.getString("tip_loga"));
                d.setVrijemePristupa(rs.getTimestamp("vrijeme_pristupa"));
                dnevnik.add(d);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(prijavaBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(prijavaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dnevnik;
    }

    public List<Dnevnik> getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(List<Dnevnik> dnevnik) {
        this.dnevnik = dnevnik;
    }
    public int tablicaBrojRedaka() {
        Konfiguracija konf = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
        int tablicaBrojRedaka = Integer.parseInt(konf.dajPostavku("tablica.brojRedaka"));
        return tablicaBrojRedaka;
    }

    public int getBrojRedaka() {
        return brojRedaka;
    }

    public void setBrojRedaka(int brojRedaka) {
        this.brojRedaka = brojRedaka;
    }
}
