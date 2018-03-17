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
INSERT INTO `amministratori` VALUES (2,7);
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
  CONSTRAINT `ProprietarioCarta` FOREIGN KEY (`idUtente`) REFERENCES `utenti` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carte_di_credito`
--

LOCK TABLES `carte_di_credito` WRITE;
/*!40000 ALTER TABLE `carte_di_credito` DISABLE KEYS */;
INSERT INTO `carte_di_credito` VALUES (92,'08475034','2027-01-09','666'),(103,'0987543452','2024-10-01','7543'),(65,'238976253','1997-10-10','853'),(60,'34523452345','2023-01-01','809'),(101,'365948453','2022-03-23','3909'),(2,'89234234','1990-10-24','777');
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
  `abilitato` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`idUtente`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (2,'root','dc76e9f0c0006e8f919e0c515c66dbba3982f785','root@gmail.com','Admin','Root','1995-10-29','34234653',1000,1),(3,'34g','ergg','mario@test.it','Dario','Rossi','2017-05-09','56745764567',0,1),(4,'sewrg','34g','gino@test.it','Gino','Pino','2017-05-09','45764576',0,1),(5,'asdgf4','seaweg','marco@test.it','Marco','Test','2017-05-09','54674567',0,1),(7,'j65er5','5ej6','mozzo@test.it','Capitano','Navale','2017-05-09','779679679',0,1),(8,'e5j6e','5ej6','capitano@test.it','Zozzo','Ubriaco','2017-05-09','45764576',0,1),(9,'dh465e','5e6j','martello@test.it','Vite','Spanata','2017-05-09','67965969',0,1),(28,'erge','erger','filippo@test.it','Filippo','Hippo','2017-05-09','4563457437',0,1),(29,'e56j','e5j6j65','h3po@test.it','Norman','Randi','2017-05-09','2457475',1,1),(55,'342523','g453g','hi45ppo@test.it','Giorgio','Mastrota','2017-05-09','45364356',4,1),(56,'gs45','4g5','hip5po@test.it','Gianni','Morandi','2017-05-09','54674567',4,1),(57,'e5g3','sg5er','hip2po@test.it','Gino','Ginoso','2017-05-09','24564356',5,1),(58,'e5g45','esg5','hi1ppo@test.it','Nomi','ACaso','2017-05-09','43564356',6,1),(59,'eg5e423','e5ghew5','hi5ppo@test.it','Peppe','Peluria','2017-05-09','6574567',7,1),(60,'erg','e5he','hi4ppo@test.it','Cinese','Gassoso','2020-01-01','777777777',0,1),(62,'test','test','prova','Luca','Sambuca','1996-11-20','3278690997',1000,1),(65,'test1','test','test@gtgest.it','Testolino','Successfull','2018-03-22','3774574',14,1),(66,'albero','nero','balber@bin.com','Alfio','Ceneri','1975-02-21','4352345',60,1),(67,'peppe','posta','peppepostino@poste.it','Peppe','Postino','1784-06-03','35254325',4,1),(68,'giag','fweg','gianio@juju.it','Gigi','Tastiera','1964-09-01','23543252',0,1),(69,'asjoweg','nowenf','hude@kurl.it','Geenos','Androide','1988-02-17','435324534',0,1),(70,'onep','onep','mantello_pelato@live.it','Saitama','Pelato','1990-04-25','43523453',1,1),(81,'436224536','onep','email1@server.dominio','Gianni','Hugo','1964-09-01','64536435',0,1),(82,'etwrhw4rt','onep','email2@server.dominio','Gilberto','Drugo','1964-09-01','45364356',0,1),(83,'ewrgewrg','onep','emai3l@server.dominio','Gulag','Pollu','1964-09-01','4576456456',0,1),(84,'eweewrg','onep','emai4l@server.dominio','Genny','Ron','1964-09-01','677777',0,1),(85,'serh','onep','email5@server.dominio','Gina','Benzina','1964-09-01','56657567',0,1),(86,'rth','onep','em3ail@server.dominio','Giga','Byte','1964-09-01','456465753',0,1),(87,'juyt','onep','emai2l@server.dominio','Guga','Ruga','1964-09-01','4564256456',0,1),(88,'ted','onep','em1ail@server.dominio','Greg','Pennarello','1981-09-01','3451451345',100,1),(89,'gnj','onep','ema1il@ser4ver.dominio','Geppetto','Mastro','1964-09-01','4564365',0,1),(90,'fdgty','onep','ema9il@server.dominio','Greta','Bruna','1964-09-10','223456645',0,1),(91,'bordi','bordi','bordi','ciccio','bordi','1996-10-10','322254',0,1),(92,'stach','18c66b36973dab974b2e422b529aab29e59f84ee','lorenzo.stacchio@studenti.unicam.it','Lorenzo','Stacchietti','1996-11-09','6666666',100,1),(94,'test2','109f4b3c50d7b0df729d299bc6f8e9ef9066971f','test@test.test','Testolaccia','Testata','2008-03-21','4564567456',0,1),(101,'empo','422068876de3222d7e1f569cae5583204fa79cc6','ruggeri85n@gmail.com','Nicolò','Ruggeri','1995-10-24','3345968526',0,1),(102,'Libro','18c66b36973dab974b2e422b529aab29e59f84ee','libbrone@one.it','Libro','Importante','2018-03-05','456456',0,1),(103,'Drago','e42fce0d4849d8fb9c28a71c993d36d3abb91f78','dragone@ciccione.it','Drago','Matto','2018-03-23','435345345',100,1),(104,'ergnergon','2d1a91a7239c50bb2eed020e3dc9eeb13c9240ca','giggio@gmail.com','23423','435345','2018-03-27','456t456',0,0),(105,'roberto','49089b61d0b15f16834bb4cf3a5fa45b7dfe1a74','robbyrobboso@gmail.com','Roberto','Robboso','2018-03-12','456456456',0,0),(109,'Nicolò','68b5193fd0f5308baac9d9eed453a89e6925bcf9','nicolo.ruggeri@studenti.unicam.it','Nicolò','Ruggeri','2018-03-21','435435',0,1),(110,'username','5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8','ruggeri5n@gmail.com','Nicolò','Ruggeri','2018-03-22','3345968526',0,1),(111,'rocco','be057d4ca44c10a0fc1dfcffd99cce1490291dc7','ruggeri95n@gmail.com','Dubai','Genova','2018-03-17','4563463656',0,1),(112,'Testa','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','gigi@g.i','testsette','Ratti','1196-01-01','464684843',0,0),(113,'bullone','34718edb825480e8d91a0c4b6d580b1e3f35b4d9','rattolino@rat.it','ratto','bine','1886-03-01','664646818',0,0),(114,'tata','90795a0ffaa8b88c0e250546d8439bc9c31e5a5e','giggifig@gmail.it','ratti','buffa','1995-06-12','64676764',0,0);
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verifica_email`
--

DROP TABLE IF EXISTS `verifica_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verifica_email` (
  `id_utente` int(11) NOT NULL,
  `codice` varchar(128) NOT NULL,
  PRIMARY KEY (`id_utente`),
  CONSTRAINT `utenteAssociato` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verifica_email`
--

LOCK TABLES `verifica_email` WRITE;
/*!40000 ALTER TABLE `verifica_email` DISABLE KEYS */;
INSERT INTO `verifica_email` VALUES (112,'b73dfe25b4b8714c029b37a6ad3006fa'),(113,'e07413354875be01a996dc560274708e'),(114,'15d4e891d784977cacbfcbb00c48f133');
/*!40000 ALTER TABLE `verifica_email` ENABLE KEYS */;
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
/*!50001 VIEW `admin_view` AS select `utenti`.`idUtente` AS `idUtente`,`utenti`.`username` AS `username`,`utenti`.`password` AS `password`,`utenti`.`email` AS `email`,`utenti`.`nome` AS `nome`,`utenti`.`cognome` AS `cognome`,`amministratori`.`livelloAmministrazione` AS `livelloAmministrazione` from (`utenti` join `amministratori`) where (`utenti`.`idUtente` = `amministratori`.`idUtenteAmministratore`) */;
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
/*!50001 VIEW `autisti_view` AS select `utenti`.`idUtente` AS `idUtente`,`carte_di_credito`.`numeroCarta` AS `numeroCarta`,date_format(`carte_di_credito`.`dataDiScadenza`,'%Y-%m-%d') AS `dataDiScadenza`,`carte_di_credito`.`pinDiVerifica` AS `pinDiVerifica`,`utenti`.`username` AS `username`,`utenti`.`password` AS `password`,`utenti`.`email` AS `email`,`utenti`.`nome` AS `nome`,`utenti`.`cognome` AS `cognome`,date_format(`utenti`.`dataDiNascita`,'%Y-%m-%d') AS `dataDiNascita`,`utenti`.`telefono` AS `telefono`,`utenti`.`saldo` AS `saldo` from (`utenti` left join `carte_di_credito` on((`utenti`.`idUtente` = `carte_di_credito`.`idUtente`))) where (`utenti`.`abilitato` = 1) */;
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

-- Dump completed on 2018-03-17 19:16:00
