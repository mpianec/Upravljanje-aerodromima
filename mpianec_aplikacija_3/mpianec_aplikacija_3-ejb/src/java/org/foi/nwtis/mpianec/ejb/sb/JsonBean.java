/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mpianec.ejb.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import org.foi.nwtis.mpianec.web.podaci.JsonPoruka;

/**
 *
 * @author Matija
 */
@Singleton
@LocalBean
public class JsonBean {

    private List<JsonPoruka> lista;
    String datoteka;

    public JsonBean() {
        lista=new ArrayList<>();
    }
    public void dodajPoruku(JsonPoruka poruka){
        lista.add(poruka);
    }

    public List<JsonPoruka> getLista() {
        return lista;
    }
    
}
