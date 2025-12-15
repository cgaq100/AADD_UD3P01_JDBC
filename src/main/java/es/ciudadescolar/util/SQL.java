package es.ciudadescolar.util;

public class SQL {
    protected static final String SACAR_INGREDIENTES_POR_NOMPIZZA = "SELECT nombre_ingrediente AS 'ingredientes' " + //
                                                                    "FROM ingrediente i, pizza_ingrediente pi, pizza p " + //
                                                                    "WHERE i.cod_ingrediente = pi.ingredienteId " + //
                                                                    "AND pi.pizzaId = p.cod_pizza " + //
                                                                    "AND p.nombre_pizza = ?;" //
    ;



}
