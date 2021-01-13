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
import org.foi.nwtis.mpianec.web.podaci.Korisnik;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;

/**
 *
 * @author Matija
 */
@Named(value = "pregledKorisnika")
@SessionScoped
public class pregledKorisnika implements Serializable {

    private List<Korisnik> korisnici = new ArrayList<>();
    private int brojRedaka;

    /**
     * Creates a new instance of pregledKorisnika
     */
    public pregledKorisnika() {
        korisnici = preuzmiKorisnike();
        brojRedaka = tablicaBrojRedaka();
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public List<Korisnik> preuzmiKorisnike() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List<Korisnik> listaKorisnika = new ArrayList<>();
        try {
            String upit = "Select * from korisnici";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Korisnik k = new Korisnik();
                k.setEmail(rs.getString("email"));
                k.setId(rs.getInt("id"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setKorime(rs.getString("korime"));
                k.setLozinka(rs.getString("lozinka"));
                listaKorisnika.add(k);
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
        return listaKorisnika;
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
