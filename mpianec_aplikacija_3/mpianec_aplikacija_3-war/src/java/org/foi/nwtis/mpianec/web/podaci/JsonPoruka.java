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
public class JsonPoruka {
    private int id;
    private String komanda;
    private String sadrzajKomande;
    private Timestamp vrijeme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKomanda() {
        return komanda;
    }

    public void setKomanda(String komanda) {
        this.komanda = komanda;
    }

    public String getSadrzajKomande() {
        return sadrzajKomande;
    }

    public void setSadrzajKomande(String sadrzajKomande) {
        this.sadrzajKomande = sadrzajKomande;
    }

    public Timestamp getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Timestamp vrijeme) {
        this.vrijeme = vrijeme;
    }
}
