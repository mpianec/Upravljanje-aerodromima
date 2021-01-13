/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.podaci;

import java.util.List;
import org.foi.nwtis.mpianec.ws.klijent.Korisnik;

/**
 *
 * @author Matija
 */
public class KorisniciResponse {

    public KorisniciResponse() {
    }
    private List<Korisnik> odgovor;
    private String status;
    private String poruka;

    public List<Korisnik> getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(List<Korisnik> odgovor) {
        this.odgovor = odgovor;
    }

    public String getStatus() {
        return status;
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

    public KorisniciResponse(String status, String poruka) {
        this.status = status;
        this.poruka = poruka;
    }

    public KorisniciResponse(List<Korisnik> odgovor, String status) {
        this.odgovor = odgovor;
        this.status = status;
    }
    
    
}
