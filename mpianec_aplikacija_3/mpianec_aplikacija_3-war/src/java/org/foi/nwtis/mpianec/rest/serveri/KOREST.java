/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.mpianec.web.podaci.KorResponse;
import org.foi.nwtis.mpianec.web.podaci.KorisniciResponse;
import org.foi.nwtis.mpianec.web.podaci.KorisnikResponse;
import org.foi.nwtis.mpianec.ws.klijent.Korisnik;

/**
 * REST Web Service
 *
 * @author Matija
 */
@Path("korisnickoIme")
public class KOREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KOREST
     */
    public KOREST() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("korime") String korime, @QueryParam("lozinka") String lozinka) {
        List<Korisnik> listaKor = new ArrayList<>();
        listaKor = podaciOKorisniku(korime, lozinka);
        KorisniciResponse odg = new KorisniciResponse();
        String odgovor = "";
        if (!listaKor.isEmpty()) {
            odg.setOdgovor(listaKor);
            odg.setStatus("OK");
        } else {
            odg.setPoruka("Pogreška");
            odg.setStatus("ERR");
        }
        Gson gson = new Gson();
        odgovor = gson.toJson(odg);
        return odgovor;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJson(String request) {
        Gson gson = new Gson();
        KorResponse odg = new KorResponse();
        Korisnik koris = gson.fromJson(request, Korisnik.class);
        if (dodajKorisnika(koris)) {
            odg.setOdgovor("[]");
            odg.setStatus("OK");
        } else {
            odg.setPoruka("Pogreška!");
            odg.setStatus("ERR");
        }
        String odgovor = gson.toJson(odg);
        return odgovor;
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonId(@PathParam("id") String korime, @QueryParam("auth") String auth) {
        List<Korisnik> listaKor = new ArrayList<>();
        Korisnik kor = new Korisnik();
        JsonObject jo = new JsonParser().parse(auth).getAsJsonObject();
        String lozinka = jo.get("lozinka").getAsString();
        listaKor = podaciOKorisniku(korime, lozinka);
        Korisnik korisnik = new Korisnik();
        korisnik=null;
        KorisnikResponse odg = new KorisnikResponse();
        String odgovor = "";
        for (Korisnik ko : listaKor) {
            if (ko.getKorime().equals(korime)  && ko.getLozinka().equals(lozinka) ) {
                korisnik = ko;
                break;
            }
        }
        if (korisnik != null) {
            odg.setStatus("OK");
            odg.setOdgovor(korisnik);
        } else {
            odg.setPoruka("Pogreška!");
            odg.setStatus("ERR");
        }
        Gson gson = new Gson();
        odgovor = gson.toJson(odg);
        return odgovor;
    }

 @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(@PathParam("id") String id,String request) {
        String odgovor = "";
        Gson gson=new Gson();
        JsonObject jo=gson.fromJson(request,JsonObject.class);
        String pass=jo.get("lozinka").getAsString();
        String ime=jo.get("ime").getAsString();
        String prezime=jo.get("prezime").getAsString();
        String email=jo.get("email").getAsString();
        Korisnik k=new Korisnik();
        k.setEmail(email);
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setLozinka(pass);
        k.setKorime(id);
        KorResponse odg = new KorResponse();
       if (azurirajKorisnika(k)) {
            odg.setOdgovor("[]");
            odg.setStatus("OK");
        } else {
            odg.setPoruka("Pogreška!");
            odg.setStatus("ERR");
        }
        odgovor = gson.toJson(odg);
        return odgovor;
    }

    private static boolean dodajKorisnika(org.foi.nwtis.mpianec.ws.klijent.Korisnik korisnik) {
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service service = new org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service();
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS port = service.getSOAPWSPort();
        return port.dodajKorisnika(korisnik);
    }

    private static java.util.List<org.foi.nwtis.mpianec.ws.klijent.Korisnik> podaciOKorisniku(java.lang.String korime, java.lang.String lozinka) {
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service service = new org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service();
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS port = service.getSOAPWSPort();
        return port.podaciOKorisniku(korime, lozinka);
    }

    private static boolean azurirajKorisnika(org.foi.nwtis.mpianec.ws.klijent.Korisnik korisnik) {
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service service = new org.foi.nwtis.mpianec.ws.klijent.SOAPWS_Service();
        org.foi.nwtis.mpianec.ws.klijent.SOAPWS port = service.getSOAPWSPort();
        return port.azurirajKorisnika(korisnik);
    }

    

}
