/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.dretve;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.mpianec.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mpianec.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mpianec.web.podaci.Aerodrom;
import org.foi.nwtis.mpianec.web.podaci.Baza;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

/**
 *
 * @author Matija
 */
public class PreuzimanjeAviona extends Thread {

    Konfiguracija konfiguracija = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
    int inicijalniPocetakIntervala;
    int pocetakIntervala;
    int krajIntervala;
    int trajanjeIntervala;
    int ciklusDretve;
    int pocetakDretve;
    int redniBrojCiklusa;
    String openskyPass;
    String openskyUsername;
    ResultSet rs = null;
    PreparedStatement ps = null;
    Connection con = null;
    String upit = "";
    List<AvionLeti> departure = new ArrayList<>();
    static volatile AtomicBoolean iduciCiklus;
    static volatile AtomicBoolean kraj;
    String put;
    Konfiguracija konfigJson;
    Gson gson = new Gson();

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @SuppressWarnings("null")
    private void inicijalizacija() {
        iduciCiklus = new AtomicBoolean(false);
        kraj = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        inicijalizacija();
        while (kraj.get() == false) {
            long pocetak = System.currentTimeMillis();
            if (iduciCiklus.get() == false) {
                try {
                    if (pocetakIntervala >= pocetak) {
                        pocetakIntervala = inicijalniPocetakIntervala;
                    }
                    List<Aerodrom> aerodromi = new ArrayList<>();
                    aerodromi = dohvatiAerodrome(rs, ps, con);
                    OSKlijent oSKlijent = new OSKlijent(openskyUsername, openskyPass);
                    for (Aerodrom aerodrom : aerodromi) {
                        departure.addAll(oSKlijent.getDepartures(aerodrom.getIcao(), pocetakIntervala, krajIntervala));
                    }
                    upit = "INSERT INTO AIRPLANES (icao24,firstseen,estdepartureairport,"
                            + "lastseen,estarrivalairport,callsign,estdepartureairporthorizdistance,"
                            + "estdepartureairportvertdistance,estarrivalairporthorizdistance,estarrivalairportvertdistance,"
                            + "departureairportcandidatescount,arrivalairportcandidatescount,stored)"
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    for (AvionLeti avion : departure) {
                        if (avion.getCallsign() != null || avion.getEstArrivalAirport() != null || avion.getEstDepartureAirport() != null || avion.getIcao24() != null) {
                            String provjera = "SELECT icao24,lastseen FROM AIRPLANES WHERE icao24=? AND lastseen=?";
                            con = Baza.dajBazu();
                            ps = con.prepareStatement(provjera);
                            ps.setString(1, avion.getIcao24());
                            ps.setInt(2, avion.getLastSeen());
                            rs = ps.executeQuery();
                            AvionLeti a = new AvionLeti();
                            List<AvionLeti> lista = new ArrayList<>();
                            while (rs.next()) {
                                AvionLeti av = new AvionLeti();
                                av.setIcao24(rs.getString("icao24"));
                                av.setLastSeen(rs.getInt("lastseen"));
                                lista.add(av);
                            }
                            ps.close();
                            rs.close();
                            con.close();
                            if (lista.size() != 0) {
                            } else {
                                dodajAvione(avion, rs, ps, con, upit);
                            }
                        } else {
                            System.out.println("Nije moguće unijeti avion");
                        }
                    }
                    pocetakIntervala = krajIntervala;
                    krajIntervala = inicijalniPocetakIntervala + trajanjeIntervala;
                    redniBrojCiklusa++;
                    if (datoteka()) {
                        FileOutputStream out = new FileOutputStream(put);
                        JsonObject jo = new JsonObject();
                        jo.addProperty("pocetakIntervala", pocetakIntervala);
                        jo.addProperty("redniBrojCiklusa", redniBrojCiklusa);
                        String zapis = gson.toJson(jo);
                        out.write(zapis.getBytes());

                    } else {
                        konfigJson = KonfiguracijaApstraktna.kreirajKonfiguraciju(put);
                        FileOutputStream out = new FileOutputStream(put);
                        JsonObject jo = new JsonObject();
                        jo.addProperty("pocetakIntervala", pocetakIntervala);
                        jo.addProperty("redniBrojCiklusa", redniBrojCiklusa);
                        String zapis = gson.toJson(jo);
                        out.write(zapis.getBytes());

                    }
                    long trajanje = (new Date().getTime()) - pocetak;
                    Thread.sleep(Math.abs(ciklusDretve - trajanje));
                } catch (InterruptedException | NemaKonfiguracije | NeispravnaKonfiguracija | IOException | ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
        openskyUsername = konfiguracija.dajPostavku("OpenSkyNetwork.korisnik");
        openskyPass = konfiguracija.dajPostavku("OpenSkyNetwork.lozinka");
        redniBrojCiklusa = 0;
        inicijalniPocetakIntervala = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.pocetak"));
        trajanjeIntervala = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.trajanje")) * 60 * 60;
        ciklusDretve = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.ciklus")) * 1000 * 60;
        pocetakIntervala = (int) (new Date().getTime() / 1000) - (inicijalniPocetakIntervala * 60 * 60);
        krajIntervala = pocetakIntervala + trajanjeIntervala;
        if (datoteka()) {
            try {
                konfigJson = KonfiguracijaApstraktna.preuzmiKonfiguraciju(put);
                pocetakIntervala = Integer.parseInt(konfigJson.dajPostavku("pocetakIntervala")) * 60000;
                redniBrojCiklusa = Integer.parseInt(konfigJson.dajPostavku("redniBrojCiklusa"));
            } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
                Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean datoteka() {
        String dat = konfiguracija.dajPostavku("datoteka");
        put = Slusac.getSc().getRealPath("/WEB-INF") + File.separator + dat;
        File f = new File(put);
        if (f.exists()) {
            return true;
        }
        return false;
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

    public void dodajAvione(AvionLeti avion, ResultSet rs, PreparedStatement ps, Connection con, String upit) {
        try {
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit);
            ps.setString(1, avion.getIcao24());
            ps.setInt(2, avion.getFirstSeen());
            ps.setString(3, avion.getEstDepartureAirport());
            ps.setInt(4, avion.getLastSeen());
            ps.setString(5, avion.getEstArrivalAirport());
            ps.setString(6, avion.getCallsign());
            ps.setInt(7, avion.getEstDepartureAirportHorizDistance());
            ps.setInt(8, avion.getEstDepartureAirportVertDistance());
            ps.setInt(9, avion.getEstArrivalAirportHorizDistance());
            ps.setInt(10, avion.getEstArrivalAirportVertDistance());
            ps.setInt(11, avion.getDepartureAirportCandidatesCount());
            ps.setInt(12, avion.getArrivalAirportCandidatesCount());
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(13, ts);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Avion nije unesen");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(PreuzimanjeAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
