package org.foi.nwtis.mpianec.web.podaci;


import java.util.List;
import org.foi.nwtis.mpianec.web.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matija
 */
public class AerodromResponse {
    private String status;
    private String poruka;
    private Aerodrom odgovor;

    public AerodromResponse(String status, String poruka) {
        this.status = status;
        this.poruka = poruka;
    }

    

    public AerodromResponse() {
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

    public Aerodrom getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(Aerodrom odgovor) {
        this.odgovor = odgovor;
    }

    
    
}
