CREATE DATABASE  IF NOT EXISTS `parkingdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `parkingdb`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: parkingdb
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `abbonamenti`
--

DROP TABLE IF EXISTS `abbonamenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `abbonamenti` (
  `idAbbonamento` int(11) NOT NULL AUTO_INCREMENT,
  `idUtente` int(11) NOT NULL,
  `dataInizio` date NOT NULL,
  `dataFine` date NOT NULL,
  PRIMARY KEY (`idAbbonamento`),
  KEY `idUtente_idx` (`idUtente`),
  CONSTRAINT `UtenteAbbonato` FOREIGN KEY (`idUtente`) REFERENCES `utenti` (`idUtente`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `abbonamenti`
--

LOCK TABLES `abbonamenti` WRITE;
/*!40000 ALTER TABLE `abbonamenti` DISABLE KEYS */;
/*!40000 ALTER TABLE `abbonamenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `admin_view`
--

DROP TABLE IF EXISTS `admin_view`;
/*!50001 DROP VIEW IF EXISTS `admin_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `admin_view` AS SELECT 
 1 AS `idUtente`,
 1 AS `username`,
 1 AS `password`,
 1 AS `email`,
 1 AS `nome`,
 1 AS `cognome`,
 1 AS `dataDiNascita`,
 1 AS `telefono`,
 1 AS `saldo`,
 1 AS `idUtenteAmministratore`,
 1 AS `livelloAmministrazione`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `amministratori`
--

DROP TABLE IF EXISTS `amministratori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amministratori` (
  `idUtenteAmministratore` int(11) NOT NULL,
  `livelloAmministrazione` tinyint(1) NOT NULL,
  PRIMARY KEY (`idUtenteAmministratore`),
  CONSTRAINT `AmministratoreAssociato` FOREIGN KEY (`idUtenteAmministratore`) REFERENCES `utenti` (`idUtente`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amministratori`
--

LOCK TABLES `amministratori` WRITE;
/*!40000 ALTER TABLE `amministratori` DISABLE KEYS */;
INSERT INTO `amministratori` VALUES (2,1);
/*!40000 ALTER TABLE `amministratori` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `autisti_view`
--

DROP TABLE IF EXISTS `autisti_view`;
/*!50001 DROP VIEW IF EXISTS `autisti_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `autisti_view` AS SELECT 
 1 AS `idUtente`,
 1 AS `numeroCarta`,
 1 AS `dataDiScadenza`,
 1 AS `pinDiVerifica`,
 1 AS `username`,
 1 AS `password`,
 1 AS `email`,
 1 AS `nome`,
 1 AS `cognome`,
 1 AS `dataDiNascita`,
 1 AS `telefono`,
 1 AS `saldo`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `carte_di_credito`
--

DROP TABLE IF EXISTS `carte_di_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carte_di_credito` (
  `idUtente` int(11) NOT NULL,
  `numeroCarta` varchar(32) NOT NULL,
  `dataDiScadenza` date NOT NULL,
  `pinDiVerifica` varchar(8) NOT NULL,
  PRIMARY KEY (`numeroCarta`,`dataDiScadenza`,`idUtente`),
  KEY `ProprietarioCarta` (`idUtente`),
  CONSTRAINT `ProprietarioCarta` FOREIGN KEY (`idUtente`) REFERENCES `utenti` (`idUtente`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carte_di_credito`
--

LOCK TABLES `carte_di_credito` WRITE;
/*!40000 ALTER TABLE `carte_di_credito` DISABLE KEYS */;
INSERT INTO `carte_di_credito` VALUES (3,'2349865239863','2026-01-12','0764'),(1,'34534703752307523','2020-10-10','234');
/*!40000 ALTER TABLE `carte_di_credito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parcheggi`
--

DROP TABLE IF EXISTS `parcheggi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parcheggi` (
  `idParcheggo` int(11) NOT NULL,
  `coordinataX` varchar(64) NOT NULL,
  `coordinataY` varchar(64) NOT NULL,
  `citta` varchar(32) NOT NULL,
  `cap` int(11) NOT NULL,
  `via` varchar(64) NOT NULL,
  `numero_civico` varchar(8) NOT NULL,
  `nPostiMacchina` int(11) NOT NULL DEFAULT '0',
  `nPostiCamper` int(11) NOT NULL DEFAULT '0',
  `nPostiMoto` int(11) NOT NULL DEFAULT '0',
  `nPostiAutobus` int(11) NOT NULL DEFAULT '0',
  `nPostiDisabile` int(11) NOT NULL DEFAULT '0',
  `tariffaOrariaLavorativi` double NOT NULL,
  `tariffaOrariaFestivi` double NOT NULL,
  `provincia` varchar(3) NOT NULL,
  PRIMARY KEY (`idParcheggo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parcheggi`
--

LOCK TABLES `parcheggi` WRITE;
/*!40000 ALTER TABLE `parcheggi` DISABLE KEYS */;
INSERT INTO `parcheggi` VALUES (0,'1.00000','2.00000','Camerino',60078,'Via parcheggio','10',40,10,20,5,7,2,1,'MC'),(1,'7.0234234','4.234534','Roma',52043,'Via test dei fragment','6A',70,50,50,20,30,1.5,3,'RO');
/*!40000 ALTER TABLE `parcheggi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazioni_pagate`
--

DROP TABLE IF EXISTS `prenotazioni_pagate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prenotazioni_pagate` (
  `idPrenotazione` int(11) NOT NULL,
  `idUtente` int(11) NOT NULL,
  `idParcheggio` int(11) NOT NULL,
  `dataPrenotazione` date NOT NULL,
  `orePermanenza` int(11) NOT NULL,
  `tipoParcheggio` int(11) NOT NULL,
  `incasso` double NOT NULL DEFAULT '0',
  KEY `Autista_idx` (`idUtente`),
  KEY `ParcheggioAssociato_idx` (`idParcheggio`),
  KEY `TipoParcheggioAssociato_idx` (`tipoParcheggio`),
  CONSTRAINT `Autista` FOREIGN KEY (`idUtente`) REFERENCES `utenti` (`idUtente`) ON UPDATE CASCADE,
  CONSTRAINT `ParcheggioAssociato` FOREIGN KEY (`idParcheggio`) REFERENCES `parcheggi` (`idParcheggo`) ON UPDATE CASCADE,
  CONSTRAINT `TipoParcheggioAssociato` FOREIGN KEY (`tipoParcheggio`) REFERENCES `tipo_parcheggio` (`idTipo_parcheggio`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazioni_pagate`
--

LOCK TABLES `prenotazioni_pagate` WRITE;
/*!40000 ALTER TABLE `prenotazioni_pagate` DISABLE KEYS */;
/*!40000 ALTER TABLE `prenotazioni_pagate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_parcheggio`
--

DROP TABLE IF EXISTS `tipo_parcheggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_parcheggio` (
  `idTipo_parcheggio` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(16) NOT NULL,
  PRIMARY KEY (`idTipo_parcheggio`),
  UNIQUE KEY `nome_UNIQUE` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_parcheggio`
--

LOCK TABLES `tipo_parcheggio` WRITE;
/*!40000 ALTER TABLE `tipo_parcheggio` DISABLE KEYS */;
INSERT INTO `tipo_parcheggio` VALUES (3,'Autobus'),(1,'Camper'),(4,'Disabile'),(0,'Macchina'),(2,'Moto');
/*!40000 ALTER TABLE `tipo_parcheggio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utenti` (
  `idUtente` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(64) NOT NULL,
  `nome` varchar(32) NOT NULL,
  `cognome` varchar(32) NOT NULL,
  `dataDiNascita` date DEFAULT NULL,
  `telefono` varchar(16) DEFAULT NULL,
  `saldo` double DEFAULT NULL,
  PRIMARY KEY (`idUtente`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,'test','test','test@gtest.com','Testolino','Successfull','1990-10-10','12986234',0),(2,'root','root','root@gmail.com','Admin','Root',NULL,NULL,NULL),(3,'brutto','bruttissimo','bruttissimome@gbrutto.it','Bruto','Romano','1986-12-12','423535435',100);
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'parkingdb'
--

--
-- Dumping routines for database 'parkingdb'
--

--
-- Final view structure for view `admin_view`
--

/*!50001 DROP VIEW IF EXISTS `admin_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `admin_view` AS select `utenti`.`idUtente` AS `idUtente`,`utenti`.`username` AS `username`,`utenti`.`password` AS `password`,`utenti`.`email` AS `email`,`utenti`.`nome` AS `nome`,`utenti`.`cognome` AS `cognome`,`utenti`.`dataDiNascita` AS `dataDiNascita`,`utenti`.`telefono` AS `telefono`,`utenti`.`saldo` AS `saldo`,`amministratori`.`idUtenteAmministratore` AS `idUtenteAmministratore`,`amministratori`.`livelloAmministrazione` AS `livelloAmministrazione` from (`utenti` join `amministratori`) where (`utenti`.`idUtente` = `amministratori`.`idUtenteAmministratore`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `autisti_view`
--

/*!50001 DROP VIEW IF EXISTS `autisti_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `autisti_view` AS select `utenti`.`idUtente` AS `idUtente`,`carte`.`numeroCarta` AS `numeroCarta`,`carte`.`dataDiScadenza` AS `dataDiScadenza`,`carte`.`pinDiVerifica` AS `pinDiVerifica`,`utenti`.`username` AS `username`,`utenti`.`password` AS `password`,`utenti`.`email` AS `email`,`utenti`.`nome` AS `nome`,`utenti`.`cognome` AS `cognome`,`utenti`.`dataDiNascita` AS `dataDiNascita`,`utenti`.`telefono` AS `telefono`,`utenti`.`saldo` AS `saldo` from (`carte_di_credito` `carte` join `utenti`) where (`utenti`.`idUtente` = `carte`.`idUtente`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-16 19:39:01
