/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.1.38-MariaDB : Database - dds_tbt
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dds_tbt` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `dds_tbt`;

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
  `dni` int(10) NOT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `cliente` */

insert  into `cliente`(`idCliente`,`nombre`,`apellido`,`email`,`contrasenia`,`usuario`,`fechaNacimiento`,`idTipoCliente`,`dni`) values (1,'Brian','Rios','brios@itport.com.ar','1234','brios','1994-12-14',1,39387297),(2,'Timo','Pasinato','timo.pas@gmail.com','1234','tpasinato','1994-01-01',1,123123);

/*Table structure for table `cuenta` */

DROP TABLE IF EXISTS `cuenta`;

CREATE TABLE `cuenta` (
  `idCuenta` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoCuenta` int(11) NOT NULL,
  `idTipoMoneda` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  PRIMARY KEY (`idCuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `cuenta` */

insert  into `cuenta`(`idCuenta`,`idTipoCuenta`,`idTipoMoneda`,`idCliente`) values (1,1,1,1),(2,1,2,1),(3,1,1,2);

/*Table structure for table `estado_cliente` */

DROP TABLE IF EXISTS `estado_cliente`;

CREATE TABLE `estado_cliente` (
  `idEstadoCliente` int(11) NOT NULL AUTO_INCREMENT,
  `idTipoEstadoCliente` int(11) NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `idCliente` int(11) NOT NULL,
  PRIMARY KEY (`idEstadoCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `estado_cliente` */

insert  into `estado_cliente`(`idEstadoCliente`,`idTipoEstadoCliente`,`fechaInicio`,`fechaFin`,`idCliente`) values (1,1,'2019-04-01','9999-12-31',1),(2,1,'2019-04-01','9999-12-31',2);

/*Table structure for table `movimiento_efectivo` */

DROP TABLE IF EXISTS `movimiento_efectivo`;

CREATE TABLE `movimiento_efectivo` (
  `idMovimientoEfectivo` int(11) NOT NULL AUTO_INCREMENT,
  `idCuenta` int(11) NOT NULL,
  `idTipoMovimiento` int(6) NOT NULL,
  `monto` decimal(12,2) NOT NULL,
  `fechaHora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idMovimientoEfectivo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `movimiento_efectivo` */

insert  into `movimiento_efectivo`(`idMovimientoEfectivo`,`idCuenta`,`idTipoMovimiento`,`monto`,`fechaHora`) values (1,1,1,8000.00,'2019-04-01 14:54:20'),(2,3,1,8000.00,'2019-04-01 10:54:44');

/*Table structure for table `tipo_cliente` */

DROP TABLE IF EXISTS `tipo_cliente`;

CREATE TABLE `tipo_cliente` (
  `idTipoCliente` int(11) NOT NULL AUTO_INCREMENT,
  `tipoCliente` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_cliente` */

insert  into `tipo_cliente`(`idTipoCliente`,`tipoCliente`) values (1,'Particular'),(2,'Comerciante'),(3,'Empresa');

/*Table structure for table `tipo_cuenta` */

DROP TABLE IF EXISTS `tipo_cuenta`;

CREATE TABLE `tipo_cuenta` (
  `idTipoCuenta` int(11) NOT NULL AUTO_INCREMENT,
  `tipoCuenta` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoCuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_cuenta` */

insert  into `tipo_cuenta`(`idTipoCuenta`,`tipoCuenta`) values (1,'Caja de ahorro');

/*Table structure for table `tipo_estado_cliente` */

DROP TABLE IF EXISTS `tipo_estado_cliente`;

CREATE TABLE `tipo_estado_cliente` (
  `idTipoEstadoCliente` int(11) NOT NULL AUTO_INCREMENT,
  `tipoEstadoCliente` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoEstadoCliente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_estado_cliente` */

insert  into `tipo_estado_cliente`(`idTipoEstadoCliente`,`tipoEstadoCliente`) values (1,'Normal');

/*Table structure for table `tipo_moneda` */

DROP TABLE IF EXISTS `tipo_moneda`;

CREATE TABLE `tipo_moneda` (
  `idTipoMoneda` int(11) NOT NULL AUTO_INCREMENT,
  `tipoMoneda` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoMoneda`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_moneda` */

insert  into `tipo_moneda`(`idTipoMoneda`,`tipoMoneda`) values (1,'Peso terraplanista'),(2,'Dolar');

/*Table structure for table `tipo_movimiento_efectivo` */

DROP TABLE IF EXISTS `tipo_movimiento_efectivo`;

CREATE TABLE `tipo_movimiento_efectivo` (
  `idTipoMovimiento` int(11) NOT NULL AUTO_INCREMENT,
  `tipoMovimiento` varchar(20) NOT NULL,
  PRIMARY KEY (`idTipoMovimiento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `tipo_movimiento_efectivo` */

insert  into `tipo_movimiento_efectivo`(`idTipoMovimiento`,`tipoMovimiento`) values (1,'Deposito'),(2,'Extraccion');

/*Table structure for table `transaccion` */

DROP TABLE IF EXISTS `transaccion`;

CREATE TABLE `transaccion` (
  `idTransaccion` int(11) NOT NULL AUTO_INCREMENT,
  `idCuentaOrigen` int(11) NOT NULL,
  `idCuentaDestino` int(11) NOT NULL,
  `monto` decimal(12,2) NOT NULL,
  `fechaHora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idTransaccion`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `transaccion` */

insert  into `transaccion`(`idTransaccion`,`idCuentaOrigen`,`idCuentaDestino`,`monto`,`fechaHora`) values (1,3,1,1000.00,'2019-04-02 14:55:15');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
