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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.mpianec.ejb.sb.JsonBean;
import org.foi.nwtis.mpianec.web.podaci.JsonPoruka;

/**
 *
 * @author Matija
 */
@Named(value = "jMSPoruke")
@SessionScoped
public class JMSPoruke implements Serializable {

    @EJB
    private JsonBean jsonBean;

    private List<JsonPoruka> lista;

    /**
     * Creates a new instance of JMSPoruke
     */
    public JMSPoruke() {
        lista = new ArrayList<>();
    }

    public String primiPoruke() {
        try {
            lista = jsonBean.getLista();
            ExternalContext kontekst = FacesContext.getCurrentInstance().getExternalContext();
            kontekst.redirect(((HttpServletRequest) kontekst.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(JMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "pregledJMSPoruka";
    }

    public String obrisiPoruku(JsonPoruka poruka) {
        try {
            lista.remove(poruka);
            ExternalContext kontekst = FacesContext.getCurrentInstance().getExternalContext();
            kontekst.redirect(((HttpServletRequest) kontekst.getRequest()).getRequestURI());
        } catch (IOException ex) {
            Logger.getLogger(JMSPoruke.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
