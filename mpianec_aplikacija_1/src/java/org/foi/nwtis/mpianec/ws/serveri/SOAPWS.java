/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.ws.serveri;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.web.podaci.Aerodrom;
import org.foi.nwtis.mpianec.web.podaci.Baza;
import org.foi.nwtis.mpianec.web.podaci.Korisnik;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author Matija
 */
@WebService(serviceName = "SOAPWS")
public class SOAPWS {

    private boolean provjeriKorisnika(String korime, String pass) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        boolean postoji = false;
        try {
            String upit3 = "SELECT * FROM Korisnici where korime=? and lozinka=?";
            con = Baza.dajBazu();
            ps = con.prepareStatement(upit3);
            ps.setString(1, korime);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("korime").isEmpty()) {
                    postoji = true;
                }
            }
            return postoji;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
            return postoji;
        }
    }

    @WebMethod(operationName = "podaciOAvionima")
    public AvionLeti podaciOAvionima(@WebParam(name = "aerodrom_id") String aerodrom_id, @WebParam(name = "korime") String korime, @WebParam(name = "pass") String pass) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        AvionLeti avioni = new AvionLeti();
        Connection con = null;
        if (provjeriKorisnika(korime, pass)) {
            try {

                String upit = "SELECT * From AIRPLANES Where estdepartureairport=?";
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setString(1, aerodrom_id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    AvionLeti av = new AvionLeti();
                    av.setIcao24(rs.getString("icao24"));
                    av.setLastSeen(rs.getInt("lastseen"));
                    av.setFirstSeen(rs.getInt("firstseen"));
                    av.setEstDepartureAirportVertDistance(rs.getInt("estdepartureairportvertdistance"));
                    av.setEstDepartureAirportHorizDistance(rs.getInt("estdepartureairporthorizdistance"));
                    av.setEstDepartureAirport(rs.getString("estdepartureairport"));
                    av.setEstArrivalAirportVertDistance(rs.getInt("estarrivalairportvertdistance"));
                    av.setEstArrivalAirportHorizDistance(rs.getInt("estarrivalairporthorizdistance"));
                    av.setEstArrivalAirport(rs.getString("estarrivalairport"));
                    av.setDepartureAirportCandidatesCount(rs.getInt("departureairportcandidatescount"));
                    av.setCallsign(rs.getString("callsign"));
                    av.setArrivalAirportCandidatesCount(rs.getInt("arrivalairportcandidatescount"));
                    avioni = av;
                }

                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za zadnje preuzetim podacima aviona");
                ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ps.close();
                    con.close();
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return avioni;
    }

    @WebMethod(operationName = "unosPodatakaAviona")
    public List<AvionLeti> unosPodatakaAviona(@WebParam(name = "n") int n, @WebParam(name = "aerodrom_id") String aerodrom_id, @WebParam(name = "korime") String korime, @WebParam(name = "pass") String pass) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Aerodrom> aerodromi = new ArrayList<>();
        List<AvionLeti> avioni = new ArrayList<>();
        Connection con = null;
        if (provjeriKorisnika(korime, pass)) {
            try {

                String upit = "SELECT * FROM airplanes WHERE estDepartureAirport=? ORDER BY id DESC LIMIT ?";
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setInt(2, n);
                ps.setString(1, aerodrom_id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    AvionLeti av = new AvionLeti();
                    av.setIcao24(rs.getString("icao24"));
                    av.setLastSeen(rs.getInt("lastseen"));
                    av.setFirstSeen(rs.getInt("firstseen"));
                    av.setEstDepartureAirportVertDistance(rs.getInt("estdepartureairportvertdistance"));
                    av.setEstDepartureAirportHorizDistance(rs.getInt("estdepartureairporthorizdistance"));
                    av.setEstDepartureAirport(rs.getString("estdepartureairport"));
                    av.setEstArrivalAirportVertDistance(rs.getInt("estarrivalairportvertdistance"));
                    av.setEstArrivalAirportHorizDistance(rs.getInt("estarrivalairporthorizdistance"));
                    av.setEstArrivalAirport(rs.getString("estarrivalairport"));
                    av.setDepartureAirportCandidatesCount(rs.getInt("departureairportcandidatescount"));
                    av.setCallsign(rs.getString("callsign"));
                    av.setArrivalAirportCandidatesCount(rs.getInt("arrivalairportcandidatescount"));
                    avioni.add(av);
                }
                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za zadnjih n podataka o avionima");
                ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ps.close();
                    rs.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return avioni;
    }

    @WebMethod(operationName = "podaciOPoletjelima")
    public List<AvionLeti> podaciOPoletjelima(@WebParam(name = "datumOd") int datumOd, @WebParam(name = "datumDo") int datumDo, @WebParam(name = "id") String id, @WebParam(name = "korime") String korime, @WebParam(name = "pass") String pass) {
        List<AvionLeti> lista = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT=? AND (FIRSTSEEN BETWEEN ? AND ?) ORDER BY firstseen desc";
        if (provjeriKorisnika(korime, pass)) {
            try {
                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setString(1, id);
                ps.setInt(2, datumOd);
                ps.setInt(3, datumDo);
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
                    lista.add(avion);
                }
                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za podacima o letovima aviona s izabranog aerodroma");
                ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;

    }

    @WebMethod(operationName = "podaciOLetovima")
    public List<AvionLeti> podaciOLetovima(@WebParam(name = "datumOd") int datumOd, @WebParam(name = "datumDo") int datumDo, @WebParam(name = "icao") String icao, @WebParam(name = "korime") String korime, @WebParam(name = "pass") String pass) {
        List<AvionLeti> lista = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String upit = "SELECT * FROM AIRPLANES WHERE icao24=? AND (LASTSEEN BETWEEN ? AND ?) ORDER BY FIRSTSEEN desc";
        if (provjeriKorisnika(korime, pass)) {
            try {

                con = Baza.dajBazu();
                ps = con.prepareStatement(upit);
                ps.setString(1, icao);
                ps.setInt(2, datumOd);
                ps.setInt(3, datumDo);
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
                    lista.add(avion);
                }

                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za podacima letenja odabranog aviona");
                ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return lista;
    }

    @WebMethod(operationName = "podaciOLetovimaString")
    public List<String> podaciOLetovimaString(@WebParam(name = "datumOd") int datumOd, @WebParam(name = "datumDo") int datumDo, @WebParam(name = "icao") String icao, @WebParam(name = "korime") String korime, @WebParam(name = "pass") String pass) {
        List<String> aerodromi = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        if (provjeriKorisnika(korime, pass)) {
            try {

                con = Baza.dajBazu();
                String upit = "SELECT estdepartureairport FROM AIRPLANES WHERE icao24=? AND (LASTSEEN BETWEEN ? AND ?) ORDER BY FIRSTSEEN asc";
                ps = con.prepareStatement(upit);
                ps.setString(1, icao);
                ps.setInt(2, datumOd);
                ps.setInt(3, datumDo);
                rs = ps.executeQuery();
                while (rs.next()) {
                    AvionLeti av = new AvionLeti();
                    av.setEstDepartureAirport(rs.getString("estdepartureairport"));
                    aerodromi.add(av.getEstDepartureAirport());
                }

                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za podacima letova aviona - aerodromi");
                ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    con.close();
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return aerodromi;
    }

    @WebMethod(operationName = "dodajKorisnika")
    public boolean dodajKorisnika(@WebParam(name = "korisnik") Korisnik korisnik) {
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = Baza.dajBazu();
            String provjera = "Select * from korisnici where korime=?";
            ps = con.prepareStatement(provjera);
            ps.setString(1, korisnik.getKorime());
            rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                rs.close();
                return false;
            } else {
                String upit = "INSERT INTO korisnici (korime,lozinka,email,ime,prezime) VALUES (?,?,?,?,?)";
                ps = con.prepareStatement(upit);
                ps.setString(1, korisnik.getKorime());
                ps.setString(2, korisnik.getLozinka());
                ps.setString(3, korisnik.getEmail());
                ps.setString(4, korisnik.getIme());
                ps.setString(5, korisnik.getPrezime());
                ps.executeUpdate();
                ps.close();
                con.close();
                con = Baza.dajBazu();
            }
            String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
            ps = con.prepareStatement(upit2);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(1, ts);
            ps.setString(2, "Promjena podataka u bazi");
            ps.setString(3, "Dodavanje podataka korisnika");
            ps.executeUpdate();
            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SOAPWS.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @WebMethod(operationName = "azurirajKorisnika")
    public boolean azurirajKorisnika(@WebParam(name = "korisnik") Korisnik korisnik) {
        PreparedStatement ps = null;
        Connection con = null;
        String koris = "";
        //   if (provjeriKorisnika(korisnik.getKorime(), korisnik.getLozinka())) {
        try {
            con = Baza.dajBazu();
            String upit = "UPDATE korisnici SET ime=?,prezime=?,email=?,lozinka=? where korime=?";
            ps = con.prepareStatement(upit);
            koris = korisnik.getKorime();
            ps.setString(1, korisnik.getIme());
            ps.setString(2, korisnik.getPrezime());
            ps.setString(3, korisnik.getEmail());
            ps.setString(4, korisnik.getLozinka());
            ps.setString(5, koris);
            ps.executeUpdate();

            ps.close();
            con.close();
            con = Baza.dajBazu();
            String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
            ps = con.prepareStatement(upit2);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(1, ts);
            ps.setString(2, "Promjena podataka u bazi");
            ps.setString(3, "Ažuriranje podataka korisnika");
            ps.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SOAPWS.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        //    }
        return false;
    }

    @WebMethod(operationName = "podaciOKorisniku")
    public List<Korisnik> podaciOKorisniku(@WebParam(name = "korime") String korime, @WebParam(name = "lozinka") String pass) {
        List<Korisnik> lista = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        if (provjeriKorisnika(korime, pass)) {
            try {
                con = Baza.dajBazu();
                String upit = "SELECT ime,prezime,korime,email,lozinka FROM korisnici";
                ps = con.prepareStatement(upit);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Korisnik kor = new Korisnik();
                    kor.setEmail(rs.getString("email"));
                    kor.setIme(rs.getString("ime"));
                    kor.setPrezime(rs.getString("prezime"));
                    kor.setKorime(rs.getString("korime"));
                    kor.setLozinka(rs.getString("lozinka"));
                    lista.add(kor);
                }
                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za podacima korisnika");
                ps.executeUpdate();
                return lista;
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ps.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SOAPWS.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @WebMethod(operationName = "meteo")
    public MeteoPodaci meteo(@WebParam(name = "aerodrom_id") String aerodrom_id,
            @WebParam(name = "korime") String korime,
            @WebParam(name = "pass") String pass
    ) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        Aerodrom aerodrom = new Aerodrom();
        Konfiguracija konf = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
        String apiKey = konf.dajPostavku("OpenWeatherMap.apikey");
        MeteoPodaci meteo = new MeteoPodaci();
        try {
            if (provjeriKorisnika(korime, pass)) {
                con = Baza.dajBazu();
                String upit = "SELECT * FROM MYAIRPORTS WHERE ident=?";
                ps = con.prepareStatement(upit);
                ps.setString(1, aerodrom_id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Aerodrom aer = new Aerodrom();
                    aer.setDrzava(rs.getString("iso_country"));
                    aer.setNaziv(rs.getString("name"));
                    aer.setIcao(rs.getString("ident"));
                    String koordinate = rs.getString("coordinates");
                    String[] polje = koordinate.split(",\\s+");
                    Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                    aer.setLokacija(lokacija);
                    aerodrom = aer;
                }

                OWMKlijent owmKlijent = new OWMKlijent(apiKey);
                meteo = owmKlijent.getRealTimeWeather(aerodrom.getLokacija().getLatitude(), aerodrom.getLokacija().getLongitude());
                rs.close();
                ps.close();
                con.close();
                con = Baza.dajBazu();
                String upit2 = "INSERT INTO dnevnik (vrijeme_pristupa,tip_loga,opis) VALUES (?,?,?)";
                ps = con.prepareStatement(upit2);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ps.setTimestamp(1, ts);
                ps.setString(2, "Zahtjev za podacima");
                ps.setString(3, "Zahtjev za meteo podacima");
                ps.executeUpdate();
                return meteo;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SOAPWS.class
                    .getName()).log(Level.SEVERE, null, ex);
            return meteo;
        } finally {
            try {
                rs.close();
                con.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(SOAPWS.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return meteo;
    }

}
