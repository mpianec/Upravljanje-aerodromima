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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mpianec.web.podaci.Baza;

/**
 *
 * @author Matija
 */
@Named(value = "prijavaBean")
@SessionScoped
public class prijavaBean implements Serializable {

    private String korime;
    private String lozinka;

    /**
     * Creates a new instance of prijavaBean
     */
    public prijavaBean() {
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

    public String preusmjeri(){
        boolean postoji=provjeriLogin(korime, lozinka);
        if(postoji){
            try {
                HttpSession sesija=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                sesija.setAttribute("korime", korime);
                FacesContext kontekst=FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("pocetna.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(prijavaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            return "";
        }
        return "";
    }
    
    public boolean provjeriLogin(String korime, String lozinka) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        boolean postoji = false;
        try {
            String upit = "Select * from korisnici where korime=? and lozinka=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, korime);
            ps.setString(2, lozinka);
            rs = ps.executeQuery();
            if(rs.next())
                postoji=true;
            else
                postoji=false;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(prijavaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(prijavaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return postoji;
    }

}
