package es.ciudadescolar.util;

public class SQL {
    
    // nomPizza
    protected static final String SACAR_INGREDIENTES_POR_NOMPIZZA = "SELECT nombre_ingrediente AS 'ingredientes' " + //
                                                                    "FROM ingrediente i, pizza_ingrediente pi, pizza p " + //
                                                                    "WHERE i.cod_ingrediente = pi.ingredienteId " + //
                                                                    "AND pi.pizzaId = p.cod_pizza " + //
                                                                    "AND p.nombre_pizza = ?;" //
    ;

    // nomPizza
    protected static final String SACAR_PRECIO_PIZZA_POR_NOM_Y_GETPRECIOPIZZA = "SELECT getPrecioPizza(cod_pizza) " +
                                                                                "FROM pizza " + 
                                                                                "WHERE nombre_pizza = ?;"
    ;

    // (ingrediente:'pollo', pizza:'margarita', cantidad:175)
    protected static final String AÑADIR_INGREDIENTE_A_PIZZA = "CALL addIngredientePizza(?,?,?);";


}
