/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.ejb.sb;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.foi.nwtis.mpianec.web.podaci.JsonPoruka;

/**
 *
 * @author Matija
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/NWTiS_mpianec_1"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class PreuzimanjePoruka implements MessageListener {

    @EJB
    private JsonBean jsonBean;
    
    public PreuzimanjePoruka() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("Poruke: "+message.getBody(String.class));
            Gson gson=new Gson();
            JsonPoruka poruka=gson.fromJson(message.getBody(String.class), JsonPoruka.class);
            jsonBean.dodajPoruku(poruka);
        } catch (JMSException ex) {
            Logger.getLogger(PreuzimanjePoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
