/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.podaci;

import org.foi.nwtis.mpianec.ws.klijent.Korisnik;

/**
 *
 * @author Matija
 */
public class KorisnikResponse {
    private String status;
    private String poruka;
    private Korisnik odgovor;

    public String getStatus() {
        return status;
    }

    public KorisnikResponse(String status, String poruka) {
        this.status = status;
        this.poruka = poruka;
    }

    public KorisnikResponse() {
    }

    public KorisnikResponse(String status, Korisnik odgovor) {
        this.status = status;
        this.odgovor = odgovor;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Korisnik getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(Korisnik odgovor) {
        this.odgovor = odgovor;
    }
}
