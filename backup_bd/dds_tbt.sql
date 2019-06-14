/*
SQLyog Professional v13.1.1 (64 bit)
MySQL - 10.1.40-MariaDB : Database - dds_tbt
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `cliente` */

DROP TABLE IF EXISTS `cliente`;

CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `apellido` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `contrasenia` varchar(50) NOT NULL,
  `usuario` varchar(15) NOT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `idTipoCliente` int(11) NOT NULL,
  `du` int(10) NOT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `cliente` */

insert  into `cliente`(`idCliente`,`nombre`,`apellido`,`email`,`contrasenia`,`usuario`,`fechaNacimiento`,`idTipoCliente`,`du`,`direccion`) values 
(1,'Brian','Rios','brios@itport.com.ar','1234','brios','1994-12-14',1,39387297,'Chascomús 26'),
(2,'Timo','Pasinato','timo.pasi@gmail.com','1234','tpasinato','1994-01-01',1,123123,'Martín Güemes 374');

/*Table structure for table `cuenta` */

DROP TABLE IF EXISTS `cuenta`;

CREATE TABLE `cuenta` (
  `idCuenta` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoCuenta` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  PRIMARY KEY (`idCuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `cuenta` */

insert  into `cuenta`(`idCuenta`,`idTipoCuenta`,`idCliente`) values 
(1,1,1),
(3,1,2);

/*Table structure for table `estados_clientes` */

DROP TABLE IF EXISTS `estados_clientes`;

CREATE TABLE `estados_clientes` (
  `idEstado` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoEstadoCliente` int(11) NOT NULL,
  `fechaInicio` date DEFAULT NULL,
  `fechaFin` date DEFAULT NULL,
  `idCliente` int(11) NOT NULL,
  PRIMARY KEY (`idEstado`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `estados_clientes` */

insert  into `estados_clientes`(`idEstado`,`idTipoEstadoCliente`,`fechaInicio`,`fechaFin`,`idCliente`) values 
(1,1,'2019-05-01','9999-12-31',1),
(2,1,'2019-05-01','9999-12-31',2);

/*Table structure for table `movimiento` */

DROP TABLE IF EXISTS `movimiento`;

CREATE TABLE `movimiento` (
  `idMovimiento` int(11) NOT NULL AUTO_INCREMENT,
  `idCuentaOrigen` int(11) DEFAULT NULL,
  `idCuentaDestino` int(11) NOT NULL,
  `importe` decimal(12,2) NOT NULL,
  `fechaHora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `idTipoMovimiento` int(11) NOT NULL,
  `estado` tinyint(4) DEFAULT NULL COMMENT '1:pendiente 2:finalizada',
  PRIMARY KEY (`idMovimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `movimiento` */

insert  into `movimiento`(`idMovimiento`,`idCuentaOrigen`,`idCuentaDestino`,`importe`,`fechaHora`,`idTipoMovimiento`,`estado`) values 
(1,0,1,10000.00,'2019-06-13 17:37:51',1,1),
(2,NULL,3,10000.00,'2019-06-13 17:58:41',1,1),
(3,1,3,5000.00,'2019-06-13 17:58:42',2,1),
(4,3,1,2000.00,'2019-06-13 17:58:42',2,1);

/*Table structure for table `tipo_cliente` */

DROP TABLE IF EXISTS `tipo_cliente`;

CREATE TABLE `tipo_cliente` (
  `idTipoCliente` int(11) NOT NULL AUTO_INCREMENT,
  `tipoCliente` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_cliente` */

insert  into `tipo_cliente`(`idTipoCliente`,`tipoCliente`) values 
(1,'Persona física'),
(2,'Persona jurídica');

/*Table structure for table `tipo_cuenta` */

DROP TABLE IF EXISTS `tipo_cuenta`;

CREATE TABLE `tipo_cuenta` (
  `idTipoCuenta` int(11) NOT NULL AUTO_INCREMENT,
  `tipoCuenta` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoCuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_cuenta` */

insert  into `tipo_cuenta`(`idTipoCuenta`,`tipoCuenta`) values 
(1,'Caja de ahorro');

/*Table structure for table `tipo_estado_cliente` */

DROP TABLE IF EXISTS `tipo_estado_cliente`;

CREATE TABLE `tipo_estado_cliente` (
  `idTipoEstadoCliente` int(11) NOT NULL AUTO_INCREMENT,
  `tipoEstadoCliente` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoEstadoCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_estado_cliente` */

insert  into `tipo_estado_cliente`(`idTipoEstadoCliente`,`tipoEstadoCliente`) values 
(1,'Activo'),
(2,'Dado de baja');

/*Table structure for table `tipo_movimiento` */

DROP TABLE IF EXISTS `tipo_movimiento`;

CREATE TABLE `tipo_movimiento` (
  `idTipoMovimiento` int(11) NOT NULL AUTO_INCREMENT,
  `tipoMovimiento` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoMovimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_movimiento` */

insert  into `tipo_movimiento`(`idTipoMovimiento`,`tipoMovimiento`) values 
(1,'Saldo Inicial'),
(2,'Transferencia'),
(3,'Compra-Venta'),
(4,'Compra bono');

/* Function  structure for function  `f_saldoCuenta` */

/*!50003 DROP FUNCTION IF EXISTS `f_saldoCuenta` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `f_saldoCuenta`(p_idCuenta int) RETURNS decimal(12,2)
    READS SQL DATA
BEGIN
	#Declaro variables
	DECLARE	v_ingreso decimal(12,2);
	DECLARE	v_egreso DECIMAL(12,2);
	
	#Hago una consulta y el resultado lo asigno a v_ingreso
	#La logica es, sumar todos los importes de las transferencias en donde la cuenta (el id) figure como destino
	#Equivale al total de la plata que ingresó a la cuenta
	SELECT SUM(b.importe) AS ingreso
	FROM cuenta a INNER JOIN movimiento b ON a.`idCuenta`=b.`idCuentaDestino`
	WHERE a.`idCuenta` = p_idCuenta
	GROUP BY idCuenta
	into v_ingreso;
	#Para saber el total de la plata que salió de la cuenta, hago lo análogo considerando la cuenta como origen
	SELECT SUM(b.importe) AS ingreso
	FROM cuenta a INNER JOIN movimiento b ON a.`idCuenta`=b.`idCuentaOrigen`
	WHERE a.`idCuenta` = p_idCuenta
	GROUP BY idCuenta
	INTO v_egreso;
	
	#Retorno "plata que ingresó" - "plata que salió"
	return (v_ingreso-v_egreso);
    END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
