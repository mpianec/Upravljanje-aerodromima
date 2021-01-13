/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.rest.serveri;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.rest.klijenti.PostAvion;
import org.foi.nwtis.mpianec.web.dretve.PreuzimanjeAviona;
import org.foi.nwtis.mpianec.web.podaci.AerResponse;
import org.foi.nwtis.mpianec.web.podaci.Aerodrom;
import org.foi.nwtis.mpianec.web.podaci.AerodromResponse;
import org.foi.nwtis.mpianec.web.podaci.AerodromiResponse;
import org.foi.nwtis.mpianec.web.podaci.Baza;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 * REST Web Service
 *
 * @author Matija
 */
@Path("aerodromi")
public class AEREST {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AEREST
     */
    public AEREST() {
    }

   

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String odgovor = "";
        AerodromiResponse ares = new AerodromiResponse();
       
            try {
                List<Aerodrom> aerodromi = new ArrayList<>();
                String upit = "SELECT * FROM MYAIRPORTS";
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Aerodrom aerodrom = new Aerodrom();
                    aerodrom.setDrzava(rs.getString("iso_country"));
                    aerodrom.setIcao(rs.getString("ident"));
                    aerodrom.setNaziv(rs.getString("name"));
                    String koordinate = rs.getString("coordinates");
                    String[] polje = koordinate.split(",\\s+");
                    Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                    aerodrom.setLokacija(lokacija);
                    aerodrom.setVrijeme(rs.getTimestamp("stored"));
                    aerodromi.add(aerodrom);
                }
                if (aerodromi.isEmpty()) {
                    ares.setPoruka("Nema aerodroma");
                    ares.setStatus("ERR");
                } else {
                    ares.setOdgovor(aerodromi);
                    ares.setStatus("OK");
                }
                Gson gson = new Gson();
                odgovor = gson.toJson(ares);

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rs.close();
                    ps.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
                    ares.setPoruka("Pogreška!");
                    ares.setStatus("ERR");
                    Gson gson = new Gson();
                    odgovor = gson.toJson(ares);
                }
            }
        
        return odgovor;
    }

    /**
     * PUT method for updating or creating an instance of AEREST
     *
     * @param content representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String dodajAerodrom(String content) {
        String odgovor = "";
        AerResponse ares = new AerResponse();
        Gson gson = new Gson();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        PostAvion post = gson.fromJson(content, PostAvion.class);
        Aerodrom a = new Aerodrom();
        Konfiguracija konfiguracija = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
        try {
            String upit = "SELECT * FROM AIRPORTS WHERE ident=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, post.getIcao());
            rs = ps.executeQuery();
            while (rs.next()) {
                Aerodrom aerodrom = new Aerodrom();
                aerodrom.setDrzava(rs.getString("iso_country"));
                aerodrom.setIcao(rs.getString("ident"));
                aerodrom.setNaziv(rs.getString("name"));
                a = aerodrom;
            }
            con.close();
            rs.close();
            ps.close();
            String token = konfiguracija.dajPostavku("LocationIQ.token");
            LIQKlijent lIQKlijent = new LIQKlijent(token);
            a.setLokacija(lIQKlijent.getGeoLocation(a.getNaziv()));
            String upit2 = "INSERT INTO MYAIRPORTS (ident,name,iso_country,coordinates,stored) values(?,?,?,?,?)";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit2);
            ps.setString(1, a.getIcao());
            ps.setString(2, a.getNaziv());
            ps.setString(3, a.getDrzava());
            String koordinate = a.getLokacija().getLongitude() + ", " + a.getLokacija().getLatitude();
            ps.setString(4, koordinate);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(5, ts);
            ps.executeUpdate();
            ares.setOdgovor("[]");
            ares.setStatus("OK");
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException | BadRequestException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            odgovor = gson.toJson(ares);
            return odgovor;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonId(@PathParam("id") String icao) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String odgovor = "";
        Gson gson = new Gson();
        Aerodrom a = new Aerodrom();
        AerodromResponse ares = new AerodromResponse();
        try {
            String upit = "SELECT * FROM MYAIRPORTS WHERE ident=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, icao);
            rs = ps.executeQuery();
            while (rs.next()) {
                Aerodrom aerodrom = new Aerodrom();
                aerodrom.setDrzava(rs.getString("iso_country"));
                aerodrom.setIcao(rs.getString("ident"));
                aerodrom.setNaziv(rs.getString("name"));
                String koordinate = rs.getString("coordinates");
                String[] polje = koordinate.split(",\\s+");
                Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                aerodrom.setLokacija(lokacija);
                aerodrom.setVrijeme(rs.getTimestamp("stored"));
                aerodromi.add(aerodrom);
                a = aerodrom;
            }
            if (aerodromi.isEmpty()) {
                ares.setPoruka("Nema aerodroma");
                ares.setStatus("ERR");
            } else {
                ares.setOdgovor(a);
                ares.setStatus("OK");
            }
            odgovor = gson.toJson(ares);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            odgovor = gson.toJson(ares);
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return odgovor;
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(@PathParam("id") String icao, String content) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String odgovor = "";
        Gson gson = new Gson();
        JsonObject jo = new Gson().fromJson(content, JsonObject.class);
        String upit = "";
        AerResponse ares = new AerResponse();
        String naziv = jo.get("naziv").toString();
        String adresa = jo.get("adresa").toString();
        String nazivPravi = naziv.substring(1, naziv.length() - 1);
        String adresaPrava = adresa.substring(1, adresa.length() - 1);
        Konfiguracija konfiguracija = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
        Aerodrom a = new Aerodrom();
        try {
            upit = "SELECT * FROM MYAIRPORTS WHERE ident=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, icao);
            rs = ps.executeQuery();
            while (rs.next()) {
                Aerodrom aerodrom = new Aerodrom();
                aerodrom.setIcao(rs.getString("ident"));
                a = aerodrom;
            }
            con.close();
            rs.close();
            ps.close();
            String token = konfiguracija.dajPostavku("LocationIQ.token");
            LIQKlijent lIQKlijent = new LIQKlijent(token);
            a.setLokacija(lIQKlijent.getGeoLocation(adresaPrava));
            String upit2 = "UPDATE MYAIRPORTS SET COORDINATES=? WHERE IDENT=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit2);
            String noviGPS = a.getLokacija().getLatitude() + ", " + a.getLokacija().getLongitude();
            ps.setString(1, noviGPS);
            ps.setString(2, a.getIcao());
            ps.executeUpdate();
            ares.setOdgovor("[]");
            ares.setStatus("OK");
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            odgovor = gson.toJson(ares);
            return odgovor;
        } finally {
            try {
                con.close();
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteJsonId(@PathParam("id") String icao) {
        PreparedStatement ps = null;
        Connection con = null;
        int obrisano = 0;
        String odgovor = "";
        AerResponse ares = new AerResponse();
        Gson gson = new Gson();
        try {
            String upit = "DELETE FROM MYAIRPORTS WHERE ident=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, icao);
            obrisano = ps.executeUpdate();
            if (obrisano == 1) {
                ares.setOdgovor("[]");
            } else {
                ares.setStatus("OK");
            }
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            odgovor = gson.toJson(ares);
            return odgovor;
        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Path("{id}/avioni")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonIdAvioni(@PathParam("id") String icao, @QueryParam("odVremena") int odVremena, @QueryParam("doVremena") int doVremena) {
        List<AvionLeti> avioni = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String odgovor = "";
        AerodromiResponse ares = new AerodromiResponse();
        Gson gson = new Gson();
        String upit = "";

        try {
            if (odVremena == 0 && doVremena == 0) {
                upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT=?";
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setString(1, icao);
            } else {
                upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT=? AND (LASTSEEN BETWEEN ? AND ?)";
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setString(1, icao);
                ps.setInt(2, odVremena);
                ps.setInt(3, doVremena);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                AvionLeti avion = new AvionLeti();
                avion.setIcao24(rs.getString("icao24"));
                avion.setLastSeen(rs.getInt("lastseen"));
                avion.setFirstSeen(rs.getInt("firstseen"));
                avion.setEstDepartureAirportVertDistance(rs.getInt("estdepartureairportvertdistance"));
                avion.setEstDepartureAirportHorizDistance(rs.getInt("estdepartureairporthorizdistance"));
                avion.setEstDepartureAirport(rs.getString("estdepartureairport"));
                avion.setEstArrivalAirportVertDistance(rs.getInt("estarrivalairportvertdistance"));
                avion.setEstArrivalAirportHorizDistance(rs.getInt("estarrivalairporthorizdistance"));
                avion.setEstArrivalAirport(rs.getString("estarrivalairport"));
                avion.setDepartureAirportCandidatesCount(rs.getInt("departureairportcandidatescount"));
                avion.setCallsign(rs.getString("callsign"));
                avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalairportcandidatescount"));
                avioni.add(avion);
            }
            if (avioni.isEmpty()) {
                ares.setPoruka("Nema aviona");
                ares.setStatus("ERR");
            } else {
                ares.setOdgovorAv(avioni);
                ares.setStatus("OK");
            }
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            odgovor = gson.toJson(ares);
            return odgovor;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Path("{id}/avioni")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String postJsonIdAvion(@PathParam("id") String icao, String request) {
        String odgovor = "";
        AerResponse ares = new AerResponse();
        Gson gson = new Gson();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        AvionLeti post = gson.fromJson(request, AvionLeti.class);
        try {
            con.close();
            rs.close();
            ps.close();
            String upit2 = "INSERT INTO AIRPLANES (icao24,firstseen,estdepartureairport,"
                    + "lastseen,estarrivalairport,callsign,estdepartureairporthorizdistance,"
                    + "estdepartureairportvertdistance,estarrivalairporthorizdistance,estarrivalairportvertdistance,"
                    + "departureairportcandidatescount,arrivalairportcandidatescount,stored)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit2);
            ps.setString(1, post.getIcao24());
            ps.setInt(2, post.getFirstSeen());
            ps.setString(3, icao);
            ps.setInt(4, post.getLastSeen());
            ps.setString(5, post.getEstArrivalAirport());
            ps.setString(6, post.getCallsign());
            ps.setInt(7, post.getEstDepartureAirportHorizDistance());
            ps.setInt(8, post.getEstDepartureAirportVertDistance());
            ps.setInt(9, post.getEstArrivalAirportHorizDistance());
            ps.setInt(10, post.getEstArrivalAirportVertDistance());
            ps.setInt(11, post.getDepartureAirportCandidatesCount());
            ps.setInt(12, post.getArrivalAirportCandidatesCount());
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(13, ts);
            ps.executeUpdate();
            ares.setOdgovor("[]");
            ares.setStatus("OK");
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            return odgovor;
        }
    }

    @Path("{id}/avion")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteJsonIdAvion(@PathParam("id") String icao) {
        String odgovor = "";
        PreparedStatement ps = null;
        Connection con = null;
        int obrisano = 0;
        AerResponse ares = new AerResponse();
        Gson gson = new Gson();
        try {
            String upit = "DELETE FROM AIRPLANES WHERE estdepartureairport=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, icao);
            obrisano = ps.executeUpdate();
            if (obrisano != 0) {
                ares.setOdgovor("[]");
                ares.setStatus("OK");
            } else {
                ares.setPoruka("Pogreška!");
                ares.setStatus("ERR");
            }
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            return odgovor;
        }
    }

    @Path("{id}/avion/{aid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteJsonIdAvionAid(@PathParam("id") String icao, @PathParam("aid") String aid) {
        String odgovor = "";
        PreparedStatement ps = null;
        Connection con = null;
        int obrisano = 0;
        AerResponse ares = new AerResponse();
        Gson gson = new Gson();
        try {
            String upit = "DELETE FROM AIRPLANES WHERE estdepartureairport=? and icao24=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, icao);
            ps.setString(2, aid);
            obrisano = ps.executeUpdate();
            if (obrisano != 0) {
                ares.setOdgovor("[]");
                ares.setStatus("OK");
            } else {
                ares.setPoruka("Pogreška!");
                ares.setStatus("ERR");
            }
            odgovor = gson.toJson(ares);
            return odgovor;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AEREST.class.getName()).log(Level.SEVERE, null, ex);
            ares.setPoruka("Pogreška!");
            ares.setStatus("ERR");
            return odgovor;
        }
    }

    public List<Aerodrom> dohvatiAerodrome(ResultSet rs, PreparedStatement ps, Connection con) {
        List<Aerodrom> aerodromi = new ArrayList<>();
        try {

            String upit = "SELECT * FROM MYAIRPORTS";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Aerodrom aerodrom = new Aerodrom();
                aerodrom.setDrzava(rs.getString("iso_country"));
                aerodrom.setIcao(rs.getString("ident"));
                aerodrom.setNaziv(rs.getString("name"));
                String koordinate = rs.getString("coordinates");
                String[] polje = koordinate.split(",\\s+");
                Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                aerodrom.setLokacija(lokacija);
                aerodrom.setVrijeme(rs.getTimestamp("stored"));
                aerodromi.add(aerodrom);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return aerodromi;
    }

}
