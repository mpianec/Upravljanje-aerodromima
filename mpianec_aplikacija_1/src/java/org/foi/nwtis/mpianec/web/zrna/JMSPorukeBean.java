/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.web.zrna;

import com.google.gson.Gson;
import javax.jms.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Matija
 */
@Named(value = "jMSPorukeBean")
@RequestScoped
public class JMSPorukeBean {

    @Resource(mappedName="jms/NWTiS_mpianec_1")
    private Queue red;
    @Resource(mappedName="jms/NWTiS_QF_mpianec_1")
    private ConnectionFactory factory;
    private String poruka;
    
    /**
     * Creates a new instance of JMSPorukeBean
     */
    public JMSPorukeBean() {
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    public static void dodajURed(String poruka){
        try {
            InitialContext kontekst=new InitialContext();
            Queue red=(Queue)kontekst.lookup("jms/NWTiS_mpianec_1");
            QueueConnectionFactory factory=(QueueConnectionFactory)kontekst.lookup("jms/NWTiS_QF_mpianec_1");
            QueueConnection konekcija=factory.createQueueConnection();
            QueueSession sesija=konekcija.createQueueSession(false,Session.DUPS_OK_ACKNOWLEDGE);
            QueueSender pošiljaoc=sesija.createSender(red);
            pošiljaoc.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage jms=sesija.createTextMessage(poruka);
            pošiljaoc.send(jms);
            System.out.println("Poslana poruka: "+jms.getText());
            konekcija.close();
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(JMSPorukeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
