package es.ciudadescolar.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbManager {

    private static final Logger LOG = LoggerFactory.getLogger(DbManager.class);

    // los siguientes valores estaticos deben contener el identificador de los valores del properties
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";

    // valor que almacena la conexión con la BD
    private Connection con = null;


    // constructor que abre la conexión con la BD
    public DbManager(String properties)
    {
        // objeto que contiene el properties
        Properties prop = new Properties(); 

        try
        {
            // cargado del archivo .properties recibido
            prop.load(new FileInputStream(properties));

            // registrado del driver
            Class.forName(prop.getProperty(DRIVER));

            // establecimiento de la conexion con la BD, se sacan los datos del properties
            con = DriverManager.getConnection(prop.getProperty(URL), prop.getProperty(USER), prop.getProperty(PASSWORD));

            LOG.debug("Establecida la conexión satisfactoriamente");
        }
        catch (FileNotFoundException e) {LOG.error("PROP | Imposible cargar propiedades de la conexion: "+e.getMessage());} 
        catch (IOException e) {LOG.error("Error de IOException: "+e.getMessage());}
        catch (ClassNotFoundException e) {LOG.error("Registro de driver con error: "+e.getMessage());}
        catch (SQLException e) {LOG.error("Imposible establecer conexion con la BD: "+e.getMessage());}
    }



    // funcion para cerrar la BD
    public boolean CerrarBD()
    {
        boolean status = false;

        if(con != null)
        {
            try
            {
                con.close();
                LOG.debug("Cerrada la conexion satisfactoriamente");
                status = true;
            }
            catch (SQLException e){LOG.error("Error cerrando la conexion: "+e.getMessage());}
        }

        return status;
    }





    // volcar en el LOG los ingredientes de una determinada pizza (parametro -> nombre de la pizza)


    public void VolcarIngredientesPizza(String nomPizza)
    {
        LOG.info("<--Entrando en el metodo VolcarIngredientesPizza con la entrada: "+nomPizza+"-->");

        PreparedStatement pstConsulta = null;
        ResultSet rstSalida = null;

        // deve logear todo lo que salga de la consulta
        try
        {
            // prepara la consulta
            pstConsulta = con.prepareStatement(SQL.SACAR_INGREDIENTES_POR_NOMPIZZA);
            pstConsulta.setString(1, nomPizza);

            // ejecuta la consulta
            rstSalida = pstConsulta.executeQuery();

            // revisa la salida de la consulta
            while(rstSalida.next())
            {
                LOG.info("Ingrediente recuperado: "+rstSalida.getString(1));
            }
        }
        catch (SQLException e){LOG.error("Error durante la consulta parametrizada: "+e.getMessage());}
        finally
        {
            // cierra la consulta y salida al terminar de operar
            try{
                if(rstSalida != null) rstSalida.close();
                if(pstConsulta != null) pstConsulta.close();
            }
            catch (SQLException e) {LOG.error("Error liberando recursos de la consulta parametrizada: "+e.getMessage());}
        }
        LOG.info("<--Saliendo del metodo VolcarIngredientesPizza-->");
    }





    // recuperar el precio de una pizza invocando la funcion existente "getPrecioPizza": 
    // ejemplo select nombre_pizza AS 'nombre pizza', getPrecioPizza(cod_pizza) AS 'precio pizza' from pizza


    public float RecuperaPrecioPizza(String nomPizza)
    {
        LOG.info("<--Entrando en el metodo RecuperaPrecioPizza con la entrada: "+nomPizza+"-->");

        float salida = -1;


        PreparedStatement pstConsulta = null;
        ResultSet rstSalida = null;


        // solo debe devolver uno o ninguno
        try 
        {
            pstConsulta = con.prepareStatement(SQL.SACAR_PRECIO_PIZZA_POR_NOM_Y_GETPRECIOPIZZA);
            pstConsulta.setString(1, nomPizza);
            rstSalida = pstConsulta.executeQuery();

            if(rstSalida.next())
            {
                salida = rstSalida.getInt(1);
                LOG.debug("Recuperado precio de la pizza: "+nomPizza+", el valor a devolver es: "+salida);
            }
        } 
        catch (SQLException e) {LOG.error("Error durante la consulta parametrizada: "+e.getMessage());}
        finally
        {
            try{
                if(rstSalida != null) rstSalida.close();
                if(pstConsulta != null) pstConsulta.close();
            }
            catch (SQLException e) {LOG.error("Error liberando recursos de la consulta parametrizada: "+e.getMessage());}
        }


        LOG.info("<--Saliendo del metodo RecuperaPrecioPizza con la salida: "+salida+"-->");
        return salida;
    }





    // permita añadir un ingrediente y cantidad a una determinada pizza invocando procedimiento existente "addIngredientePizza": 
    // ejemplo CALL addIngredientePizza('Pollo','Margarita',175); Nota: ingrediente y pizza deben existir en la BD


    public boolean AñadirIngredienteYCantidadAPizza(String nomIngrediente, int cantidad, String nomPizza)
    {
        LOG.info("<--Entrando en el metodo AñadirIngredienteYCantidadAPizza con la entrada: "+nomIngrediente+", "+cantidad+" y "+nomPizza+"-->");

        boolean status = false;

        


        LOG.info("<--Saliendo del metodo AñadirIngredienteYCantidadAPizza con la salida: "+status+"-->");
        return status;
    }





    // Dar de alta de forma transaccional la siguiente pizza y sus ingredientes:
    // pizza 3 "Melanzana", precio 16€, ingredientes: Mozzarella 425g, Tomate 245g, Berenjena 600g, Aceite balsámico 90g


}
