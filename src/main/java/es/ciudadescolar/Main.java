package es.ciudadescolar;

import java.util.HashMap;

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

        dbManager.RecuperaPrecioPizza("Barbacoa");
        dbManager.RecuperaPrecioPizza("Margarita");

        dbManager.AñadirIngredienteYCantidadAPizza("pollo","Margarita",175);


        // pizza 3 "Melanzana", precio 16€, ingredientes: Mozzarella 425g, Tomate 245g, Berenjena 600g, Aceite balsámico 90g


        HashMap<String, Integer> ingredientes = new HashMap<>();
        ingredientes.put("Mozzarella", 425);
        ingredientes.put("Tomate", 245);
        ingredientes.put("Berenjena", 600);
        ingredientes.put("Aceite balsámico", 90);

        dbManager.AltaPizzaYSusIngredientes("Melanzana", 16, ingredientes);



        dbManager.CerrarBD();


        LOG.debug("<___ FINAL PROGRAMA ___>");

    }
}