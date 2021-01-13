/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.zrna;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import org.foi.nwtis.mpianec.web.podaci.Korisnik;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import org.foi.nwtis.mpianec.ejb.sb.StatefulBean;

/**
 *
 * @author Matija
 */
@Named(value = "prijavaRegistracija")
@SessionScoped
public class PrijavaRegistracija implements Serializable {

    private String korimeReg;
    private String lozinkaReg;
    private String korimePri;
    private String lozinkaPri;
    private String ponovljenaLoz;
    private String ime;
    private String prezime;
    private String email;
    private String poruka;
    private List<Korisnik> korisnici = new ArrayList<>();
    KOREST_JerseyClient klijent = new KOREST_JerseyClient();

    @EJB
    private StatefulBean statefulBean;

    /**
     * Creates a new instance of PrijavaRegistracija
     */
    public void registriraj() {
        if (ime.equals("") || email.equals("") || prezime.equals("") || korimeReg.equals("") || lozinkaReg.equals("") || ponovljenaLoz.equals("")) {
            poruka = "Popunite polja";
        } else {
            if (!ponovljenaLoz.equals(lozinkaReg)) {
                poruka = "Lozinke se ne podudaraju!";
            } else {
                Korisnik korisnik = new Korisnik();
                korisnik.setEmail(email);
                korisnik.setIme(ime);
                korisnik.setKorime(korimeReg);
                korisnik.setLozinka(lozinkaReg);
                korisnik.setPrezime(prezime);
                String odgovor = klijent.postJson(korisnik);
                JsonObject jo = new JsonParser().parse(odgovor).getAsJsonObject();
                String status = jo.get("status").getAsString();
                if (status.equals("OK")) {
                    poruka = "Korisnik je uspješno registriran";
                    try {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("prijavaRegistracija.xhtml");
                    } catch (IOException ex) {
                        Logger.getLogger(PrijavaRegistracija.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    poruka = "Nije moguće registrirati korisnika.";
                }
            }
        }
    }

    public void autentificiraj() {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (sesija == null) {
            try {
                FacesContext kontekst = FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("prijavaRegistracija.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RadSAerodromima.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (sesija.getAttribute("korime") == null) {
                try {
                    FacesContext kontekst = FacesContext.getCurrentInstance();
                    kontekst.getExternalContext().redirect("prijavaRegistracija.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(RadSAerodromima.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void prikaziKorisnike() {
        HttpSession sesija=(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String odgovor = klijent.getJson((String)sesija.getAttribute("lozinka"),(String) sesija.getAttribute("korime"));
        Gson gson = new Gson();
        JsonObject jo = new JsonParser().parse(odgovor).getAsJsonObject();
        String status = jo.get("status").getAsString();
        if (status.equals("OK")) {
            JsonArray jsonLista = jo.getAsJsonArray("odgovor");
            korisnici = gson.fromJson(jsonLista.toString(), ArrayList.class);
        } else {
            poruka = "Dogodila se greška";
        }
    }

    public String prijavi() {
        if (statefulBean.provjera(korimePri, lozinkaPri)) {
            try {
                HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                sesija.setAttribute("korime", korimePri);
                sesija.setAttribute("lozinka", lozinkaPri);
                FacesContext kontekst = FacesContext.getCurrentInstance();
                kontekst.getExternalContext().redirect("odabiri.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(PrijavaRegistracija.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            poruka = "Neuspješna prijava";
        }
        return "";
    }

    public void azuriraj() {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        korimeReg = (String) sesija.getAttribute("korime");
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(email);
        korisnik.setIme(ime);
        korisnik.setKorime(korimeReg);
        korisnik.setLozinka(lozinkaReg);
        korisnik.setPrezime(prezime);

        KOREST_JerseyClient klijent = new KOREST_JerseyClient();
        Korisnik request = new Korisnik();
        request.setLozinka(lozinkaReg);
        request.setEmail(email);
        request.setIme(ime);
        request.setPrezime(prezime);
        request.setKorime(korimeReg);
        Gson gson = new Gson();

        String loz = gson.toJson(request);
        if (request.getEmail().isEmpty() || request.getIme().isEmpty() || request.getKorime().isEmpty() || request.getLozinka().isEmpty() || request.getPrezime().isEmpty()) {
            poruka = "Popunite sve podatke!";
        } else {
            String odgovor = klijent.putJson(loz, korimeReg);
            JsonObject jo = new JsonParser().parse(odgovor).getAsJsonObject();
            String status = jo.get("status").getAsString();
            if (status.equals("OK")) {
                poruka = "Uspješno ažurirano";
            } else {
                poruka = "Greška kod ažuriranja";
            }
        }
    }

    public PrijavaRegistracija() {
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getKorimeReg() {
        return korimeReg;
    }

    public void setKorimeReg(String korimeReg) {
        this.korimeReg = korimeReg;
    }

    public String getLozinkaReg() {
        return lozinkaReg;
    }

    public void setLozinkaReg(String lozinkaReg) {
        this.lozinkaReg = lozinkaReg;
    }

    public String getKorimePri() {
        return korimePri;
    }

    public void setKorimePri(String korimePri) {
        this.korimePri = korimePri;
    }

    public String getLozinkaPri() {
        return lozinkaPri;
    }

    public void setLozinkaPri(String lozinkaPri) {
        this.lozinkaPri = lozinkaPri;
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public String getPonovljenaLoz() {
        return ponovljenaLoz;
    }

    public void setPonovljenaLoz(String ponovljenaLoz) {
        this.ponovljenaLoz = ponovljenaLoz;
    }

    static class KOREST_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/mpianec_aplikacija_3-war/webresources";

        public KOREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("korisnickoIme");
        }

        public String putJson(Object requestEntity, String id) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String postJson(Object requestEntity) throws ClientErrorException {
            return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String getJsonId(String id, String auth) throws ClientErrorException {
            WebTarget resource = webTarget;
            if (auth != null) {
                resource = resource.queryParam("auth", auth);
            }
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String getJson(String lozinka, String korime) throws ClientErrorException {
            WebTarget resource = webTarget;
            if (lozinka != null) {
                resource = resource.queryParam("lozinka", lozinka);
            }
            if (korime != null) {
                resource = resource.queryParam("korime", korime);
            }
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public void close() {
            client.close();
        }
    }

}
