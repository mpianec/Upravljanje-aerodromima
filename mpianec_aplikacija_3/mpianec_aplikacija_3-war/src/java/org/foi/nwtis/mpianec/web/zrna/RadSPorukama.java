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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mpianec.ejb.sb.JsonBean;
import org.foi.nwtis.mpianec.web.podaci.JsonPoruka;

/**
 *
 * @author Matija
 */
@Named(value = "radSPorukama")
@SessionScoped
public class RadSPorukama implements Serializable {

    @EJB
    JsonBean jsonBean;
    
    private List<JsonPoruka> listaPoruka=new ArrayList<>();
    
    /**
     * Creates a new instance of radSPorukama
     */
    public RadSPorukama() {
    }
     public void autentificiraj() {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija == null) {
            try {
                FacesContext kontekst = FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RadSPorukama.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (sesija.getAttribute("korime") == null) {
                try {
                    FacesContext kontekst = FacesContext.getCurrentInstance();
                    kontekst.getExternalContext().redirect("index.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(RadSPorukama.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
     
     public void preuzmiPoruke(){
         listaPoruka=jsonBean.getLista();
     }
     
     public void brišiPoruku(JsonPoruka poruka){
         listaPoruka.remove(poruka);
     }

    public List<JsonPoruka> getListaPoruka() {
        return listaPoruka;
    }

    public void setListaPoruka(List<JsonPoruka> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }
     
     
}
