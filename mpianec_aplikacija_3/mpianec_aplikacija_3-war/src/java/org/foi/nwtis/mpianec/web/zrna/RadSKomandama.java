/*
 * To change this license header, choose License Headers dis Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template dis the editor.
 */
package org.foi.nwtis.mpianec.web.zrna;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;

/**
 *
 * @author Matija
 */
@Named(value = "radSKomandama")
@SessionScoped
public class RadSKomandama implements Serializable {

    private String korisnik;
    private String lozinka;
    private final String host;
    private final int port;
    private String response;
    private Konfiguracija konf;

    /**
     * Creates a new instance of RadSKomandama
     */
    public RadSKomandama() {
        konf=(Konfiguracija)Slusac.getSc().getAttribute("Konfig");
        port = Integer.parseInt(konf.dajPostavku("port"));
        host = konf.dajPostavku("adresa");
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija.getAttribute("korime") != null) {
            korisnik = sesija.getAttribute("korime").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
        }
    }

    public void autentificiraj() {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija == null) {
            try {
                FacesContext kontekst = FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RadSKomandama.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (sesija.getAttribute("korime") == null) {
                try {
                    FacesContext kontekst = FacesContext.getCurrentInstance();
                    kontekst.getExternalContext().redirect("index.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(RadSKomandama.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void PAUZA() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; PAUZA;";
        posaljiKomandu(false, komanda);
    }

    public void KRENI() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; KRENI;";
        posaljiKomandu(false, komanda);
    }

    public void PASIVNO() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; PASIVNO;";
        posaljiKomandu(false, komanda);
    }

    public void AKTIVNO() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; AKTIVNO;";
        posaljiKomandu(false, komanda);
    }

    public void STANI() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; STANI;";
        posaljiKomandu(false, komanda);
    }

    public void STANJE() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; STANJE;";
        posaljiKomandu(false, komanda);
    }
    
    public void DODAJ() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA DODAJ;";
        posaljiKomandu(false, komanda);
    }
    
    public void PREKID() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA PREKID;";
        posaljiKomandu(false, komanda);
    }
    
    public void KRENIGRUPA() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA KRENI;";
        posaljiKomandu(false, komanda);
    }
    
    public void STANJEGRUPA() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA STANJE;";
        posaljiKomandu(false, komanda);
    }
    
    public void PAUZAGRUPA() {
        String komanda = "KORISNIK " + korisnik + "; LOZINKA " + lozinka + "; GRUPA PAUZA;";
        posaljiKomandu(false, komanda);
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void posaljiKomandu(boolean status, String komanda) {
        Socket socket = null;
        InputStream is = null;
        DataInputStream dis = null;
        OutputStream os = null;
        DataOutputStream dos = null;
        try {
            socket = new Socket(host, port);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            dos.writeUTF(komanda);
            dos.flush();
            socket.shutdownOutput();
            FacesMessage poruka = new FacesMessage(FacesMessage.SEVERITY_INFO, "Komanda uspješno poslana!", "Pričekajte...");
            FacesContext.getCurrentInstance().addMessage(null, poruka);
            StringBuffer sb = new StringBuffer();
            while (true) {
                try {
                    String odgovor = dis.readUTF();
                    sb.append(odgovor);
                } catch (EOFException ex) {
                    break;
                }
            }
            response=sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(RadSKomandama.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                socket.close();
            } catch (IOException ex) {
                System.out.println("ERROR; Problemi kod zatvaranja socketa/streamova");
            }
        }
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
