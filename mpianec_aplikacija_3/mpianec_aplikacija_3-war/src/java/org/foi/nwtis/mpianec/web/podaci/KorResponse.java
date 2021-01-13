/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.podaci;

/**
 *
 * @author Matija
 */
public class KorResponse {
    private String odgovor;
    private String status;
    private String poruka;

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
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

    public KorResponse() {
    }

    public KorResponse(String status, String poruka) {
        this.status = status;
        this.poruka = poruka;
    }
    
    
}
