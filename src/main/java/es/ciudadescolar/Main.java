package es.ciudadescolar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.util.DbManager;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        
        LOG.debug("<___ INICIO PROGRAMA ___>");


        DbManager dbManager = new DbManager("conexionBD.properties");

        dbManager.VolcarIngredientesPizza("Barbacoa");
        dbManager.VolcarIngredientesPizza("Margarita");

        dbManager.CerrarBD();


        LOG.debug("<___ FINAL PROGRAMA ___>");

    }
}