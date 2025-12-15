package es.ciudadescolar.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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

        if(con != null)
        {
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
        }

        LOG.info("<--Saliendo del metodo RecuperaPrecioPizza con la salida: "+salida+"-->");
        return salida;
    }





    // permita añadir un ingrediente y cantidad a una determinada pizza invocando procedimiento existente "addIngredientePizza": 
    // ejemplo CALL addIngredientePizza('Pollo','Margarita',175); Nota: ingrediente y pizza deben existir en la BD


    public boolean AñadirIngredienteYCantidadAPizza(String nomIngrediente,String nomPizza, int cantidad)
    {
        LOG.info("<--Entrando en el metodo AñadirIngredienteYCantidadAPizza con la entrada: "+nomIngrediente+", "+nomPizza+" y "+cantidad+"-->");

        PreparedStatement pstConsulta = null;

        boolean status = false;

        if(con != null)
        {
            try
            {
                pstConsulta = con.prepareStatement(SQL.AÑADIR_INGREDIENTE_A_PIZZA);
                // (ingrediente:'pollo', pizza:'margarita', cantidad:175)
                pstConsulta.setString(1, nomIngrediente);
                pstConsulta.setString(2, nomPizza);
                pstConsulta.setInt(3, cantidad);

                pstConsulta.executeUpdate();

                LOG.debug("Se ha realizado satisfactoriamente la insercion de "+nomIngrediente+" en la pizza: "+nomPizza+" con una cantidad de "+cantidad);
                status = true;
            }
            catch(SQLException e){LOG.error("Error durante el alta del ingrediente: "+ e.getMessage());}
            finally
            {
                if (pstConsulta != null)
                {
                    try 
                    {
                        pstConsulta.close();
                    } 
                    catch (SQLException e) {LOG.error("Error durante la liberacion de recursos en el alta transaccional: "+ e.getMessage());}
                    LOG.debug("Liberacion de recursos satisfactoria durante el alta transaccional");
                }
            }
        }

        LOG.info("<--Saliendo del metodo AñadirIngredienteYCantidadAPizza con la salida: "+status+"-->");
        return status;
    }





    // Dar de alta de forma transaccional la siguiente pizza y sus ingredientes:
    // pizza 3 "Melanzana", precio 16€, ingredientes: Mozzarella 425g, Tomate 245g, Berenjena 600g, Aceite balsámico 90g


    public boolean AltaPizzaYSusIngredientes(String nomPizza, float precio, Map<String, Integer> ingredientes)
    {
        LOG.info("<--Entrando en el metodo AltaPizzaYSusIngredientes con la entrada: "+nomPizza+", precio: "+precio+" y "+ingredientes.size()+" ingredientes-->");

        boolean status = false;

        PreparedStatement pstAlta = null;

        if(con != null)
        {
            try
            {
                // establece el autocommit en false para ser el responsable de hacer el commit o rollback
                con.setAutoCommit(false);

                // 1º pizza

                // inserta la pizza
                pstAlta = con.prepareStatement(SQL.INSERTAR_PIZZA);
                pstAlta.setString(1, nomPizza);
                pstAlta.setFloat(2, precio);

                pstAlta.executeUpdate();
                LOG.debug("Se inserto una pizza: "+nomPizza);
                pstAlta.clearParameters();


                // 2º ingredientes/pizza_ingredientes

                for(Map.Entry<String, Integer> entry : ingredientes.entrySet())
                {
                    // inserta el ingrediente
                    pstAlta = con.prepareStatement(SQL.INSERTAR_INGREDIENTE);
                    pstAlta.setString(1, entry.getKey());
                    pstAlta.setString(2, entry.getKey());

                    pstAlta.executeUpdate();
                    LOG.debug("Se inserto un ingrediente: "+entry.getKey());
                    pstAlta.clearParameters();


                    // vincula el ingrediente con la pizza
                    pstAlta = con.prepareStatement(SQL.AÑADIR_INGREDIENTE_A_PIZZA);
                    pstAlta.setString(1, entry.getKey());
                    pstAlta.setString(2, nomPizza);
                    pstAlta.setInt(3, entry.getValue());

                    pstAlta.executeUpdate();
                    LOG.debug("Se vinculo el ingrediente ["+entry.getKey()+"] con la pizza ["+nomPizza+"] con una cantidad de ["+entry.getValue()+"]");
                    pstAlta.clearParameters();
                }

                // realiza el commit
                con.commit();
                LOG.debug("Se han confirmado todos lso cambios de la transaccion");
                status = true;
            }
            catch(SQLException e) {
                LOG.error("Error durante el alta de la pizza: "+e.getMessage());
                try 
                {
                    con.rollback();
                    LOG.debug("Rollback realizado correctamente");
                } 
                catch (SQLException e1) {LOG.error("Error haciendo rollback de la transaccion: "+e.getMessage());}
            }
            finally
            {
                if (pstAlta != null)
                {
                    try 
                    {
                        pstAlta.close();
                        con.setAutoCommit(true); // restaurar el autocommit para que bajo la misma sesion, cualquier alta, baja o modificacion haga commit automaticamente
                    } 
                    catch (SQLException e) {LOG.error("Error durante la liberacion de recursos en el alta transaccional: "+ e.getMessage());}
                    LOG.debug("Liberacion de recursos satisfactoria durante el alta transaccional");
                }
            }
        }


        LOG.info("<--Saliendo del metodo AltaPizzaYSusIngredientes con la salida: "+status+"-->");
        return status;
    }




}
