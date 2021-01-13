package org.foi.nwtis.mpianec.web.dretve;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.mpianec.konfiguracije.Konfiguracija;
import org.foi.nwtis.mpianec.web.slusaci.Slusac;

/**
 *
 * @author Matija
 */
public class ServerDretva extends Thread {

    static volatile AtomicBoolean krajRada;
    private boolean iskljuci;
    private ServerSocket socket;
    private int port;
    private ServletContext context;
    private Konfiguracija konf;

    @Override
    public void interrupt() {
        krajRada.set(true);
        iskljuci = true;
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

    @Override
    public void run() {
        try {
            socket = new ServerSocket(port);
            while (krajRada.get() == false) {
                Socket korSocket = socket.accept();
                Date date = new Date();
                if (krajRada.get() == true) {
                    OutputStream os = korSocket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.writeUTF("OK 15;");
                    dos.flush();
                    korSocket.shutdownOutput();
                    break;
                } else {
                    RadnaDretva rd = new RadnaDretva(korSocket, new Timestamp(date.getTime()));
                    rd.start();
                }
            }
            while (!iskljuci) {
                Socket korSocket = socket.accept();
                OutputStream os = korSocket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.writeUTF("OK 15;");
                dos.flush();
                korSocket.shutdownOutput();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        context=Slusac.getSc();
        konf=(Konfiguracija) context.getAttribute("Konfig");
        port = Integer.parseInt(konf.dajPostavku("port"));
        krajRada = new AtomicBoolean(false);
        iskljuci = false;
        super.start();
    }

}
