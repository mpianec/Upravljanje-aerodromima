/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.podaci;

import java.sql.Timestamp;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 *
 * @author nwtis_1
 */
public class Aerodrom {
    private String icao;
    private String naziv;
    private String drzava;
    private Lokacija lokacija;
    private Timestamp vrijeme;

    public Aerodrom(String icao, String naziv, String drzava, Lokacija lokacija,Timestamp vrijeme) {
        this.icao=icao;
        this.naziv=naziv;
        this.drzava=drzava;
        this.lokacija=lokacija;
        this.vrijeme=vrijeme;
    }

    public Aerodrom() {
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

    public Timestamp getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Timestamp vrijeme) {
        this.vrijeme = vrijeme;
    }
    
    
}
