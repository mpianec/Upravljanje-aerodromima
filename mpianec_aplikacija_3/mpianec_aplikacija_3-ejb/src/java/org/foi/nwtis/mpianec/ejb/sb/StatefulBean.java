/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.ejb.sb;


import com.google.gson.Gson;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpianec.web.podaci.Korisnik;


/**
 *
 * @author Matija
 */
@Stateful
@LocalBean
public class StatefulBean {

    public boolean provjera(String korime,String lozinka){
        KOREST_JerseyClient klijent =new KOREST_JerseyClient();
        Korisnik request=new Korisnik();
        request.setLozinka(lozinka);
        Gson gson=new Gson();
        String loz=gson.toJson(request);
        String odgovor=klijent.getJsonId(korime, loz);
        JsonObject jo = new JsonParser().parse(odgovor).getAsJsonObject();
        String status=jo.get("status").getAsString();
        if(status.equals("OK"))
            return true;
        else
            return false;
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
                try {
                    resource = resource.queryParam("auth",URLEncoder.encode(auth, "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(StatefulBean.class.getName()).log(Level.SEVERE, null, ex);
                }
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
