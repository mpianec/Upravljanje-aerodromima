/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.podaci;

import java.sql.Timestamp;

/**
 *
 * @author Matija
 */
public class Dnevnik {
    private Timestamp vrijemePristupa;
    private String tipLoga;
    private String opis;
    private int id;

    public Dnevnik() {
    }

    public Dnevnik(Timestamp vrijemePristupa, String tipLoga, String opis, int id) {
        this.vrijemePristupa = vrijemePristupa;
        this.tipLoga = tipLoga;
        this.opis = opis;
        this.id = id;
    }

    
    public Timestamp getVrijemePristupa() {
        return vrijemePristupa;
    }

    public void setVrijemePristupa(Timestamp vrijemePristupa) {
        this.vrijemePristupa = vrijemePristupa;
    }

    public String getTipLoga() {
        return tipLoga;
    }

    public void setTipLoga(String tipLoga) {
        this.tipLoga = tipLoga;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
