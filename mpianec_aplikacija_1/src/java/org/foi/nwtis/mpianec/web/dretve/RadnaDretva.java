package org.foi.nwtis.mpianec.web.dretve;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jms.JMSException;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.web.podaci.JsonPoruka;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;
import org.foi.nwtis.mpianec.web.zrna.JMSPorukeBean;
import org.foi.nwtis.mpianec.ws.klijenti.StatusKorisnika;
import org.foi.nwtis.mpianec.web.podaci.Baza;
import org.foi.nwtis.mpianec.ws.klijenti.AerodromiWSKlijent;

/**
 *
 * @author Matija
 */
public class RadnaDretva extends Thread {

    private final String korreg = "^KORISNIK [a-zA-Z0-9ŠĐŽČĆšđžčć_\\-]{1,20}; LOZINKA [a-zA-Z0-9ŠĐŽČĆšđžčć_!#\\-]{1,20}; (PAUZA|KRENI|PASIVNO|AKTIVNO|STANI|STANJE);$";
    private final String grupareg = "^KORISNIK [a-zA-Z0-9ŠĐŽČĆšđžčć_\\-]{1,20}; LOZINKA [a-zA-Z0-9ŠĐŽČĆšđžčć_!#\\-]{1,20}; GRUPA (DODAJ|PREKID|KRENI|PAUZA|STANJE);$";
    private final Socket socket;
    private Connection con;
    private String zahtjev;
    private String[] dijeloviZahtjeva;
    private int vrstaZahtjeva;
    Konfiguracija konfiguracija = null;
    int brojaJMSPoruka = 1;

    public RadnaDretva(Socket socket, Timestamp timestamp) {
        try {
            this.con = Baza.dajBazu();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.socket = socket;
        konfiguracija = (Konfiguracija) Slusac.getSc().getAttribute("Konfig");
    }

    @Override
    public void interrupt() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

    @Override
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void run() {
        switch (vrstaZahtjeva) {
            case 1:
                boolean korisnikAutenticiran = autentificiraj();
                if (korisnikAutenticiran) {
                    switch (dijeloviZahtjeva[2]) {
                        case "PAUZA":
                            if (PreuzimanjeAviona.iduciCiklus.compareAndSet(false, true)) {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 10;");
                            } else {
                                saljiJMSPoruku();
                                ispisNaSocket("ERR 10;");
                            }
                            break;
                        case "KRENI":
                            if (PreuzimanjeAviona.iduciCiklus.compareAndSet(true, false)) {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 10;");
                            } else {
                                saljiJMSPoruku();
                                ispisNaSocket("ERR 11;");
                            }
                            break;
                        case "PASIVNO":
                            saljiJMSPoruku();
                            break;
                        case "AKTIVNO":
                            saljiJMSPoruku();
                            break;
                        case "STANI":
                            if (PreuzimanjeAviona.kraj.compareAndSet(false, true) && ServerDretva.krajRada.compareAndSet(false, true)) {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 10;");
                            } else {
                                saljiJMSPoruku();
                                ispisNaSocket("ERR 12;");
                            }
                            break;
                        case "STANJE":
                            if (PreuzimanjeAviona.kraj.get() == true) {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 15;");
                            } else if (PreuzimanjeAviona.iduciCiklus.get() == true) {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 13;");
                            } else {
                                saljiJMSPoruku();
                                ispisNaSocket("OK 14;");
                            }
                            break;
                    }
                }
            case 2: {
                boolean rez;
                rez = AerodromiWSKlijent.autenticirajGrupu("mpianec", "0989983926");
                if (rez == true) {
                    switch (dijeloviZahtjeva[2]) {
                        case "GRUPA DODAJ":
                            rez = AerodromiWSKlijent.registrirajGrupu("mpianec", "0989983926");
                            if (rez == true) {
                                ispisNaSocket("OK 20;");
                            } else {
                                ispisNaSocket("ERR 21;");
                            }
                            break;
                        case "GRUPA PREKID":
                            rez = AerodromiWSKlijent.deregistrirajGrupu("mpianec", "0989983926");
                            if (rez == true) {
                                ispisNaSocket("OK 20;");
                            } else {
                                ispisNaSocket("ERR 21;");
                            }
                            break;
                        case "GRUPA KRENI":
                            rez = AerodromiWSKlijent.aktivirajGrupu("mpianec", "0989983926");
                            if (rez == true) {
                                ispisNaSocket("OK 20;");
                            } else {
                                ispisNaSocket("ERR 22;");
                            }
                            break;
                        case "GRUPA PAUZA":
                            rez = AerodromiWSKlijent.blokirajGrupu("mpianec", "0989983926");
                            if (rez == true) {
                                ispisNaSocket("OK 20;");
                            } else {
                                ispisNaSocket("ERR 23;");
                            }
                            break;
                        case "GRUPA STANJE":
                            StatusKorisnika statusKorisnika = AerodromiWSKlijent.dajStatusGrupe("mpianec", "0989983926");
                            if (statusKorisnika == StatusKorisnika.NEPOSTOJI) {
                                ispisNaSocket("ERR 21;");
                            } else if (statusKorisnika == StatusKorisnika.AKTIVAN) {
                                ispisNaSocket("OK 21;");
                            } else if (statusKorisnika == StatusKorisnika.BLOKIRAN) {
                                ispisNaSocket("OK 22;");
                            } else if (statusKorisnika == StatusKorisnika.REGISTRIRAN) {
                                ispisNaSocket("OK 22;");
                            } else if (statusKorisnika == StatusKorisnika.DEREGISTRIRAN) {
                                ispisNaSocket("ERR 21;");
                            }
                            break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public synchronized void start() {
        vrstaZahtjeva = provjeriZahtjev();
        if (vrstaZahtjeva == 0) {
            ispisNaSocket("ERR 0;");
            return;
        }
        dijeloviZahtjeva = zahtjev.split(";");
        dijeloviZahtjeva[0] = dijeloviZahtjeva[0].replace("KORISNIK ", "");
        dijeloviZahtjeva[1] = dijeloviZahtjeva[1].replace(" LOZINKA ", "");
        dijeloviZahtjeva[2] = dijeloviZahtjeva[2].trim();
        try {
            con = Baza.dajBazu();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (autentificiraj() == false) {
            return;
        }
        super.start();
    }

    private void saljiJMSPoruku() {
        JsonPoruka poruka = new JsonPoruka();
        poruka.setId(brojaJMSPoruka);
        poruka.setKomanda(dijeloviZahtjeva[2]);
        poruka.setVrijeme(dohvatiTrenutnoVrijeme());
        Gson gson = new Gson();
        String odgovor = gson.toJson(poruka);
        JMSPorukeBean.dodajURed(odgovor);
        brojaJMSPoruka++;
    }

    private String dohvatiTrenutnoVrijeme() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    private boolean autentificiraj() {
        String SQLupit = "SELECT * FROM korisnici WHERE korime = ? AND lozinka = ?";
        try (PreparedStatement naredba = con.prepareStatement(SQLupit)) {
            naredba.setBytes(1, dijeloviZahtjeva[0].getBytes("UTF-8"));
            naredba.setBytes(2, dijeloviZahtjeva[1].getBytes("UTF-8"));
            ResultSet odgovor = naredba.executeQuery();
            if (odgovor.next()) {
                return true;
            }
        } catch (SQLException | UnsupportedEncodingException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        ispisNaSocket("ERR 10;");
        return false;
    }

    private void ispisNaSocket(String tekst) {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(os);
            out.writeUTF(tekst);
            out.flush();
            if (out != null) {
                out.close();
            }
            con.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private int provjeriZahtjev() {
        StringBuffer sb = null;
        Pattern pattern;
        Matcher m;
        boolean status;
        InputStream is;
        try {
            is = socket.getInputStream();
            DataInputStream in = new DataInputStream((is));
            sb = new StringBuffer();
            while (true) {
                try {
                    String odgovor = in.readUTF();
                    sb.append(odgovor);
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        zahtjev = sb.toString().trim();
        pattern = Pattern.compile(korreg);
        m = pattern.matcher(zahtjev);
        status = m.matches();
        if (status == true) {
            return 1;
        }
        pattern = Pattern.compile(grupareg);
        m = pattern.matcher(zahtjev);
        status = m.matches();
        if (status == true) {
            return 2;
        }
        return 0;
    }
}
