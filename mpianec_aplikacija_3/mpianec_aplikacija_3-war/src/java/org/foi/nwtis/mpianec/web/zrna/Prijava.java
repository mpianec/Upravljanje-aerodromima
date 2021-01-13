/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.zrna;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mpianec.ejb.sb.StatefulBean;

/**
 *
 * @author Matija
 */
@Named(value = "prijava")
@SessionScoped
public class Prijava implements Serializable {

    /**
     * Creates a new instance of prijava
     */
    public Prijava() {
    }
    private String korime;
    private String lozinka;
    private String poruka;
    
    @EJB
    private StatefulBean statefulBean;

    public String prijavi(){
        if(statefulBean.provjera(korime, lozinka)){
            try {
                HttpSession sesija=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                sesija.setAttribute("korime", korime);
                sesija.setAttribute("lozinka", lozinka);
                FacesContext kontekst=FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("pocetna.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(Prijava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            poruka="Neuspješna prijava";
        }
        return "";
    } 

    public String getKorime() {
        return korime;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
