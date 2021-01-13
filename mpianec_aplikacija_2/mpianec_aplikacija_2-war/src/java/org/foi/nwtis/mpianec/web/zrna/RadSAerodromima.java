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
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.xml.ws.WebServiceRef;
import org.foi.nwtis.mpianec.web.podaci.Aerodrom;
import org.foi.nwtis.mpianec.web.podaci.Avion;
import org.foi.nwtis.mpianec.web.podaci.PostAvion;
import org.foi.nwtis.mpianec.ws.serveri.AvionLeti;
import org.foi.nwtis.mpianec.ws.serveri.MeteoPodaci;
import org.foi.nwtis.mpianec.ws.serveri.SOAPWS_Service;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 *
 * @author Matija
 */
@Named(value = "radSAerodromima")
@SessionScoped
public class RadSAerodromima implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8084/mpianec_aplikacija_1/SOAPWS.wsdl")
    private SOAPWS_Service service;

    /**
     * Creates a new instance of RadSAerodromima
     */
    private String odabraniAerodrom;
    private List<Aerodrom> listaAerodroma;
    private Aerodrom aerodrom;
    private String icao;
    private float temperatura = 0;
    private float vlaga = 0;
    private float tlak = 0;
    private Date odVremena;
    private Date doVremena;
    private List<AvionLeti> listaAviona;
    private List<AvionLeti> listaLetova;
    private String poruka;
    private List<Avion> listaAvionVrijeme;
    private List<Avion> listaLetovaVrijeme;

    public RadSAerodromima() {
        listaAvionVrijeme=new ArrayList<>();
        listaLetovaVrijeme=new ArrayList<>();
        preuzmiAerodrome();
    }

    public String getOdabraniAerodrom() {
       
        return odabraniAerodrom;
    }

    public void setOdabraniAerodrom(String odabraniAerodrom) {
        this.odabraniAerodrom = odabraniAerodrom;
    }

    public Aerodrom getAerodrom() {
        return aerodrom;
    }

    public void setAerodrom(Aerodrom aerodrom) {
        this.aerodrom = aerodrom;
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

    public String dodajAerodromREST() {
        AEREST_JerseyClient klijent = new AEREST_JerseyClient();
        PostAvion pa = new PostAvion();
        pa.setIcao(icao);
        Gson gson = new Gson();
        String request = gson.toJson(pa);
        klijent.dodajAerodrom(request);
        preuzmiAerodrome();
        return "";
    }

    public String meteoPodaciSOAP(String icao) {
        HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        MeteoPodaci podaci = new MeteoPodaci();
        podaci = meteo(icao, (String) sesija.getAttribute("korime"), (String) sesija.getAttribute("lozinka"));
        temperatura = podaci.getTemperatureValue();
        vlaga = podaci.getHumidityValue();
        tlak = podaci.getPressureValue();
        return "";
    }

    public String formatiranje(int epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format((long) epoch * 1000);
    }

    public void dohvatiAvioneSOAP(String icao) {
        if (odVremena == null || doVremena == null) {
            poruka = "Odaberite datum!";
        } else {
            try {
                poruka = "";
                listaAvionVrijeme=new ArrayList<>();
                HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                String od = odVremena.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);
                Date novoOd = sdf.parse(od);
                int konačnoOd = (int) (novoOd.getTime() / 1000);
                String doV = doVremena.toString();
                Date novoDo = sdf.parse(doV);
                int konačnoDo = (int) (novoDo.getTime() / 1000);
                listaAviona = podaciOPoletjelima(konačnoOd, konačnoDo, icao, (String) sesija.getAttribute("korime"), (String) sesija.getAttribute("lozinka"));
                for (AvionLeti a : listaAviona) {
                    Avion avion = new Avion();
                    avion.setCallsign(a.getCallsign());
                    avion.setEstArrivalAirport(a.getEstArrivalAirport());
                    avion.setEstDepartureAirport(a.getEstDepartureAirport());
                    avion.setFirstSeen(formatiranje(a.getFirstSeen()));
                    avion.setLastSeen(formatiranje(a.getLastSeen()));
                    avion.setIcao24(a.getIcao24());
                    listaAvionVrijeme.add(avion);
                }
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            } catch (ParseException | IOException ex) {
                Logger.getLogger(RadSAerodromima.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void dohvatiLetoveSOAP(String icao) {
        try {
            listaLetovaVrijeme=new ArrayList<>();
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String od = odVremena.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);
            Date novoOd = sdf.parse(od);
            int konačnoOd = (int) (novoOd.getTime() / 1000);
            String doV = doVremena.toString();
            Date novoDo = sdf.parse(doV);
            int konačnoDo = (int) (novoDo.getTime() / 1000);
            listaLetova = podaciOLetovima(konačnoOd, konačnoDo, icao, (String) sesija.getAttribute("korime"), (String) sesija.getAttribute("lozinka"));
            for (AvionLeti a : listaLetova) {
                    Avion avion = new Avion();
                    avion.setCallsign(a.getCallsign());
                    avion.setEstArrivalAirport(a.getEstArrivalAirport());
                    avion.setEstDepartureAirport(a.getEstDepartureAirport());
                    avion.setFirstSeen(formatiranje(a.getFirstSeen()));
                    avion.setLastSeen(formatiranje(a.getLastSeen()));
                    avion.setIcao24(a.getIcao24());
                    listaLetovaVrijeme.add(avion);
                }
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (ParseException | IOException ex) {
            Logger.getLogger(RadSAerodromima.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void promjenaVrijednosti(ValueChangeEvent event) {
        try {
            odabraniAerodrom = event.getNewValue().toString();
        } catch (NullPointerException ex) {
        }
    }

    private void preuzmiAerodrome() {
        Gson gson = new Gson();
        AEREST_JerseyClient klijent = new AEREST_JerseyClient();
        String json = klijent.getJson();
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        String status = jo.get("status").getAsString();
        if (status.equals("OK")) {
            JsonArray jsonLista = jo.getAsJsonArray("odgovor");
            listaAerodroma = gson.fromJson(jsonLista.toString(), ArrayList.class);
        }
    }

    public List<Aerodrom> getListaAerodroma() {
        return listaAerodroma;
    }

    public void setListaAerodroma(List<Aerodrom> listaAerodroma) {
        this.listaAerodroma = listaAerodroma;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getTlak() {
        return tlak;
    }

    public void setTlak(float tlak) {
        this.tlak = tlak;
    }

    public float getVlaga() {
        return vlaga;
    }

    public void setVlaga(float vlaga) {
        this.vlaga = vlaga;
    }

    public List<AvionLeti> getListaAviona() {
        return listaAviona;
    }

    public void setListaAviona(List<AvionLeti> listaAviona) {
        this.listaAviona = listaAviona;
    }

    private MeteoPodaci meteo(java.lang.String aerodromId, java.lang.String korime, java.lang.String pass) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.mpianec.ws.serveri.SOAPWS port = service.getSOAPWSPort();
        return port.meteo(aerodromId, korime, pass);
    }

    public Date getOdVremena() {
        return odVremena;
    }

    public void setOdVremena(Date odVremena) {
        this.odVremena = odVremena;
    }

    public Date getDoVremena() {
        return doVremena;
    }

    public void setDoVremena(Date doVremena) {
        this.doVremena = doVremena;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public List<AvionLeti> getListaLetova() {
        return listaLetova;
    }

    public void setListaLetova(List<AvionLeti> listaLetova) {
        this.listaLetova = listaLetova;
    }

    public List<Avion> getListaAvionVrijeme() {
        return listaAvionVrijeme;
    }

    public void setListaAvionVrijeme(List<Avion> listaAvionVrijeme) {
        this.listaAvionVrijeme = listaAvionVrijeme;
    }

    public List<Avion> getListaLetovaVrijeme() {
        return listaLetovaVrijeme;
    }

    public void setListaLetovaVrijeme(List<Avion> listaLetovaVrijeme) {
        this.listaLetovaVrijeme = listaLetovaVrijeme;
    }

    static class AEREST_JerseyClient {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8084/mpianec_aplikacija_1/webresources";

        public AEREST_JerseyClient() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("aerodromi");
        }

        public String putJson(Object requestEntity, String id) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String postJsonIdAvion(Object requestEntity, String id) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}/avioni", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String getJsonIdAvioni(String id, int doVremena, int odVremena) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}/avioni", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String dodajAerodrom(Object requestEntity) throws ClientErrorException {
            return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String deleteJsonIdAvionAid(String id, String aid) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}/avion/{1}", new Object[]{id, aid})).request().delete(String.class);
        }

        public String getJsonId(String id) throws ClientErrorException {
            WebTarget resource = webTarget;
            resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String deleteJsonId(String id) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(String.class);
        }

        public String deleteJsonIdAvion(String id) throws ClientErrorException {
            return webTarget.path(java.text.MessageFormat.format("{0}/avion", new Object[]{id})).request().delete(String.class);
        }

        public String getJson() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public void close() {
            client.close();
        }
    }

    private java.util.List<org.foi.nwtis.mpianec.ws.serveri.AvionLeti> podaciOLetovima(int datumOd, int datumDo, java.lang.String icao, java.lang.String korime, java.lang.String pass) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.mpianec.ws.serveri.SOAPWS port = service.getSOAPWSPort();
        return port.podaciOLetovima(datumOd, datumDo, icao, korime, pass);
    }

    private java.util.List<org.foi.nwtis.mpianec.ws.serveri.AvionLeti> podaciOPoletjelima(int datumOd, int datumDo, java.lang.String id, java.lang.String korime, java.lang.String pass) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.foi.nwtis.mpianec.ws.serveri.SOAPWS port = service.getSOAPWSPort();
        return port.podaciOPoletjelima(datumOd, datumDo, id, korime, pass);
    }

}
