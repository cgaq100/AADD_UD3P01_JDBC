SET GLOBAL log_bin_trust_function_creators = 1;
drop database IF EXISTS pizzas;
create database pizzas character set utf8mb4 collate utf8mb4_0900_ai_ci;
use pizzas;

create table pizza(
cod_pizza smallint unsigned auto_increment,
nombre_pizza VARCHAR(100) NOT NULL,
precio decimal(6,2) unsigned NOT NULL default 0.00,
constraint pk_pizza PRIMARY KEY (cod_pizza)
);

create table ingrediente(
cod_ingrediente smallint unsigned auto_increment,
nombre_ingrediente varchar(50) NOT NULL,
constraint pk_ingrediente PRIMARY KEY (cod_ingrediente)
);

create table pizza_ingrediente
(
pizzaId smallint unsigned,
ingredienteId smallint unsigned,
cantidad decimal(6,2) unsigned default 0.0,
constraint fk_pizza FOREIGN KEY (pizzaId) REFERENCES pizza(cod_pizza),
constraint fk_ingrediente FOREIGN KEY (ingredienteId) REFERENCES ingrediente(cod_ingrediente),
constraint pk_pizzaing PRIMARY KEY (pizzaId,ingredienteId)

);

INSERT INTO pizza(cod_pizza, nombre_pizza,precio) values (1, 'Barbacoa', 8.0);
INSERT INTO pizza(cod_pizza, nombre_pizza,precio) values (2, 'Margarita', 6.0);
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (1,'Salsa Barbacoa');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (2,'Mozzarella');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (3,'Pollo');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (4,'Bacon');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (5,'Ternera');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (6,'Tomate');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (7,'Jamón');
INSERT INTO ingrediente(cod_ingrediente, nombre_ingrediente) values (8,'Bacon');

INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (1,1,250);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (1,2,50.5);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (1,3,250);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (1,4,100);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (1,5,400);

INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (2,6,250);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (2,7,150);
INSERT INTO pizza_ingrediente(pizzaId, ingredienteId, cantidad) VALUES (2,8,500);


DELIMITER $$
DROP FUNCTION IF EXISTS getPrecioPizza $$
CREATE FUNCTION getPrecioPizza(pizza smallint unsigned)
  RETURNS DECIMAL(6.2) 
BEGIN
    RETURN (SELECT precio FROM pizza WHERE cod_pizza=pizza);
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS addIngredientePizza $$
CREATE PROCEDURE addIngredientePizza(nombreIngrediente VARCHAR(50), nombrePizza VARCHAR(100), cantidad decimal(6.2))
BEGIN
    DECLARE idPizza SMALLINT unsigned;
    DECLARE idIngrediente SMALLINT unsigned;
    DECLARE mensaje VARCHAR(128);
    IF NOT EXISTS(SELECT * FROM pizza WHERE UPPER(nombre_pizza) = UPPER(nombrePizza))
    THEN
		SET mensaje = concat('No existe pizza: ',nombrePizza);
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT =mensaje;
	ELSEIF NOT EXISTS(SELECT * FROM ingrediente WHERE UPPER(nombre_ingrediente) = UPPER(nombreIngrediente))
    THEN
		SET mensaje = concat('No existe ingrediente: ',nombrePizza);
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT =mensaje;
	ELSE
		SELECT cod_pizza into idPizza FROM pizza WHERE UPPER(nombre_pizza) = UPPER(nombrePizza);
        SELECT cod_ingrediente into idIngrediente FROM ingrediente WHERE UPPER(nombre_ingrediente) = UPPER(nombreIngrediente);
		INSERT INTO pizza_ingrediente VALUES (idPizza,idIngrediente,cantidad);
    END IF;
END $$
DELIMITER ;

CALL addIngredientePizza('Pollo','Margarita',175);
