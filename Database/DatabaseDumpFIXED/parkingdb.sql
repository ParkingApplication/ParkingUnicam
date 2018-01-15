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
/*!40000 ALTER TABLE `amministratori` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`idParcheggo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parcheggi`
--

LOCK TABLES `parcheggi` WRITE;
/*!40000 ALTER TABLE `parcheggi` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`idUtente`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'parkingdb'
--

--
-- Dumping routines for database 'parkingdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-15 21:56:33
