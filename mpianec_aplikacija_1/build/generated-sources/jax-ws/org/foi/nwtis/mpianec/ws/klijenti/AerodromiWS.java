
package org.foi.nwtis.mpianec.ws.klijenti;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.2
 * Generated source version: 2.2
 * 
 */
@WebService(name = "AerodromiWS", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AerodromiWS {


    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ucitajUgradeneAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.UcitajUgradeneAerodromeGrupe")
    @ResponseWrapper(localName = "ucitajUgradeneAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.UcitajUgradeneAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/ucitajUgradeneAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/ucitajUgradeneAerodromeGrupeResponse")
    public boolean ucitajUgradeneAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ucitajUgradeneAvioneGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.UcitajUgradeneAvioneGrupe")
    @ResponseWrapper(localName = "ucitajUgradeneAvioneGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.UcitajUgradeneAvioneGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/ucitajUgradeneAvioneGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/ucitajUgradeneAvioneGrupeResponse")
    public boolean ucitajUgradeneAvioneGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param avionNovi
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dodajAvionGrupi", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajAvionGrupi")
    @ResponseWrapper(localName = "dodajAvionGrupiResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajAvionGrupiResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajAvionGrupiRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajAvionGrupiResponse")
    public boolean dodajAvionGrupi(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "avionNovi", targetNamespace = "")
        Avion avionNovi);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @param serodrom
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dodajAerodromGrupi", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajAerodromGrupi")
    @ResponseWrapper(localName = "dodajAerodromGrupiResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajAerodromGrupiResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajAerodromGrupiRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajAerodromGrupiResponse")
    public Boolean dodajAerodromGrupi(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "serodrom", targetNamespace = "")
        Aerodrom serodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param drzavaAerodrom
     * @param idAerodrom
     * @param nazivAerodrom
     * @param lonAerodrom
     * @param korisnickoIme
     * @param latAerodrom
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dodajNoviAerodromGrupi", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajNoviAerodromGrupi")
    @ResponseWrapper(localName = "dodajNoviAerodromGrupiResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DodajNoviAerodromGrupiResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajNoviAerodromGrupiRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dodajNoviAerodromGrupiResponse")
    public Boolean dodajNoviAerodromGrupi(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom,
        @WebParam(name = "nazivAerodrom", targetNamespace = "")
        String nazivAerodrom,
        @WebParam(name = "drzavaAerodrom", targetNamespace = "")
        String drzavaAerodrom,
        @WebParam(name = "latAerodrom", targetNamespace = "")
        String latAerodrom,
        @WebParam(name = "lonAerodrom", targetNamespace = "")
        String lonAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param idAerodrom
     * @param korisnickoIme
     * @return
     *     returns java.util.List<org.foi.nwtis.mpianec.ws.klijenti.Avion>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajSveAvioneAerodromaGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajSveAvioneAerodromaGrupe")
    @ResponseWrapper(localName = "dajSveAvioneAerodromaGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajSveAvioneAerodromaGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajSveAvioneAerodromaGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajSveAvioneAerodromaGrupeResponse")
    public List<Avion> dajSveAvioneAerodromaGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obrisiSveAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiSveAerodromeGrupe")
    @ResponseWrapper(localName = "obrisiSveAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiSveAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiSveAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiSveAerodromeGrupeResponse")
    public Boolean obrisiSveAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param idAerodrom
     * @param korisnickoIme
     * @return
     *     returns org.foi.nwtis.mpianec.ws.klijenti.AerodromStatus
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajStatusAerodromaGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajStatusAerodromaGrupe")
    @ResponseWrapper(localName = "dajStatusAerodromaGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajStatusAerodromaGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajStatusAerodromaGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajStatusAerodromaGrupeResponse")
    public AerodromStatus dajStatusAerodromaGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param avioniNovi
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "postaviAvioneGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PostaviAvioneGrupe")
    @ResponseWrapper(localName = "postaviAvioneGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PostaviAvioneGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/postaviAvioneGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/postaviAvioneGrupeResponse")
    public boolean postaviAvioneGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "avioniNovi", targetNamespace = "")
        List<Avion> avioniNovi);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "autenticirajGrupu", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AutenticirajGrupu")
    @ResponseWrapper(localName = "autenticirajGrupuResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AutenticirajGrupuResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/autenticirajGrupuRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/autenticirajGrupuResponse")
    public Boolean autenticirajGrupu(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "registrirajGrupu", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.RegistrirajGrupu")
    @ResponseWrapper(localName = "registrirajGrupuResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.RegistrirajGrupuResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/registrirajGrupuRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/registrirajGrupuResponse")
    public Boolean registrirajGrupu(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.util.List<org.foi.nwtis.mpianec.ws.klijenti.Aerodrom>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajSveAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajSveAerodromeGrupe")
    @ResponseWrapper(localName = "dajSveAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajSveAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajSveAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajSveAerodromeGrupeResponse")
    public List<Aerodrom> dajSveAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deregistrirajGrupu", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DeregistrirajGrupu")
    @ResponseWrapper(localName = "deregistrirajGrupuResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DeregistrirajGrupuResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/deregistrirajGrupuRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/deregistrirajGrupuResponse")
    public Boolean deregistrirajGrupu(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "aktivirajGrupu", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajGrupu")
    @ResponseWrapper(localName = "aktivirajGrupuResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajGrupuResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajGrupuRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajGrupuResponse")
    public Boolean aktivirajGrupu(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "blokirajGrupu", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajGrupu")
    @ResponseWrapper(localName = "blokirajGrupuResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajGrupuResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajGrupuRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajGrupuResponse")
    public Boolean blokirajGrupu(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns org.foi.nwtis.mpianec.ws.klijenti.StatusKorisnika
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajStatusGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajStatusGrupe")
    @ResponseWrapper(localName = "dajStatusGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajStatusGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajStatusGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajStatusGrupeResponse")
    public StatusKorisnika dajStatusGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param idAerodrom
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "aktivirajAerodromGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajAerodromGrupe")
    @ResponseWrapper(localName = "aktivirajAerodromGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajAerodromGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajAerodromGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajAerodromGrupeResponse")
    public boolean aktivirajAerodromGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param idAerodrom
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "blokirajAerodromGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajAerodromGrupe")
    @ResponseWrapper(localName = "blokirajAerodromGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajAerodromGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajAerodromGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajAerodromGrupeResponse")
    public boolean blokirajAerodromGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param idAerodrom
     * @param korisnickoIme
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obrisiAerodromGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiAerodromGrupe")
    @ResponseWrapper(localName = "obrisiAerodromGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiAerodromGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiAerodromGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiAerodromGrupeResponse")
    public boolean obrisiAerodromGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "idAerodrom", targetNamespace = "")
        String idAerodrom);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @param odabraniAerodromi
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "aktivirajOdabraneAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajOdabraneAerodromeGrupe")
    @ResponseWrapper(localName = "aktivirajOdabraneAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.AktivirajOdabraneAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajOdabraneAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/aktivirajOdabraneAerodromeGrupeResponse")
    public boolean aktivirajOdabraneAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "odabraniAerodromi", targetNamespace = "")
        List<String> odabraniAerodromi);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @param odabranaAerodromi
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "blokirajOdabraneAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajOdabraneAerodromeGrupe")
    @ResponseWrapper(localName = "blokirajOdabraneAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.BlokirajOdabraneAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajOdabraneAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/blokirajOdabraneAerodromeGrupeResponse")
    public boolean blokirajOdabraneAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "odabranaAerodromi", targetNamespace = "")
        List<String> odabranaAerodromi);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @param odabraniAerodromi
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "obrisiOdabraneAerodromeGrupe", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiOdabraneAerodromeGrupe")
    @ResponseWrapper(localName = "obrisiOdabraneAerodromeGrupeResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.ObrisiOdabraneAerodromeGrupeResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiOdabraneAerodromeGrupeRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/obrisiOdabraneAerodromeGrupeResponse")
    public boolean obrisiOdabraneAerodromeGrupe(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "odabraniAerodromi", targetNamespace = "")
        List<String> odabraniAerodromi);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajBrojPoruka", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajBrojPoruka")
    @ResponseWrapper(localName = "dajBrojPorukaResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajBrojPorukaResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajBrojPorukaRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajBrojPorukaResponse")
    public int dajBrojPoruka(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param korisnickaLozinka
     * @param korisnickoIme
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dajTrajanjeCiklusa", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajTrajanjeCiklusa")
    @ResponseWrapper(localName = "dajTrajanjeCiklusaResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.DajTrajanjeCiklusaResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajTrajanjeCiklusaRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/dajTrajanjeCiklusaResponse")
    public int dajTrajanjeCiklusa(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka);

    /**
     * 
     * @param brojPoruka
     * @param korisnickaLozinka
     * @param korisnickoIme
     */
    @WebMethod
    @RequestWrapper(localName = "promjeniBrojPoruka", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PromjeniBrojPoruka")
    @ResponseWrapper(localName = "promjeniBrojPorukaResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PromjeniBrojPorukaResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/promjeniBrojPorukaRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/promjeniBrojPorukaResponse")
    public void promjeniBrojPoruka(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "brojPoruka", targetNamespace = "")
        int brojPoruka);

    /**
     * 
     * @param korisnickaLozinka
     * @param trajanjeCiklusa
     * @param korisnickoIme
     */
    @WebMethod
    @RequestWrapper(localName = "promjeniTrajanjeCiklusa", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PromjeniTrajanjeCiklusa")
    @ResponseWrapper(localName = "promjeniTrajanjeCiklusaResponse", targetNamespace = "http://serveri.ws.dkermek.nwtis.foi.org/", className = "org.foi.nwtis.mpianec.ws.klijenti.PromjeniTrajanjeCiklusaResponse")
    @Action(input = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/promjeniTrajanjeCiklusaRequest", output = "http://serveri.ws.dkermek.nwtis.foi.org/AerodromiWS/promjeniTrajanjeCiklusaResponse")
    public void promjeniTrajanjeCiklusa(
        @WebParam(name = "korisnickoIme", targetNamespace = "")
        String korisnickoIme,
        @WebParam(name = "korisnickaLozinka", targetNamespace = "")
        String korisnickaLozinka,
        @WebParam(name = "trajanjeCiklusa", targetNamespace = "")
        int trajanjeCiklusa);

}
