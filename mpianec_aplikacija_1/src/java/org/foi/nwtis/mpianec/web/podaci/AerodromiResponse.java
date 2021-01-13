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
public class AerodromiResponse {
    private List<Aerodrom> odgovor;
    private List<AvionLeti> odgovorAv;
    private String status;
    private String poruka;
    private Aerodrom aerodrom;

    public AerodromiResponse(String status, String poruka) {
        this.status = status;
        this.poruka = poruka;
    }

    public List<Aerodrom> getOdgovor() {
        return odgovor;
    }

    public AerodromiResponse(List<Aerodrom> odgovor, String status) {
        this.odgovor = odgovor;
        this.status = status;
    }

    public AerodromiResponse() {
    }

    public void setOdgovor(List<Aerodrom> odgovor) {
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

    public List<AvionLeti> getOdgovorAv() {
        return odgovorAv;
    }

    public void setOdgovorAv(List<AvionLeti> odgovorAv) {
        this.odgovorAv = odgovorAv;
    }

    public Aerodrom getAerodrom() {
        return aerodrom;
    }

    public void setAerodrom(Aerodrom aerodrom) {
        this.aerodrom = aerodrom;
    }
    
}
