-- MySQL dump 10.13  Distrib 5.1.37, for debian-linux-gnu (i486)
--
-- Host: localhost    Database: konuj
-- ------------------------------------------------------
-- Server version	5.1.37-1ubuntu5

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
-- Table structure for table `chat_transcripts`
--

DROP TABLE IF EXISTS `chat_transcripts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_transcripts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user1_cookie_id` text NOT NULL,
  `user2_cookie_id` text NOT NULL,
  `user1_ip` text CHARACTER SET latin1 NOT NULL,
  `user2_ip` text CHARACTER SET latin1 NOT NULL,
  `user1_join_time` datetime NOT NULL,
  `user2_join_time` datetime NOT NULL,
  `transcript` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_transcripts`
--

LOCK TABLES `chat_transcripts` WRITE;
/*!40000 ALTER TABLE `chat_transcripts` DISABLE KEYS */;
INSERT INTO `chat_transcripts` VALUES (3,'cookie_id','','ip1','ip2','2010-01-17 00:00:00','0000-00-00 00:00:00','User-2 : sdfdf\n'),(4,'cookie_id','','ip1','ip2','2010-01-17 00:00:00','0000-00-00 00:00:00','User-2 : hello\nUser-1 : hi\nUser-1 : how r u ?\nUser-2 : i m good\nUser-2 : what abt u\nUser-2 : and where are u from\nUser-1 : i m from indore\n'),(5,'cookie_id','','ip1','ip2','2010-01-17 00:00:00','0000-00-00 00:00:00','User-2 : hello\nUser-2 : hi\nUser-1 : hi\nUser-2 : how r u?\nUser-1 : asl pls?\nUser-2 : sdlfsfdf\n'),(6,'cookie_id','','ip1','ip2','2010-01-18 00:00:00','0000-00-00 00:00:00','User-2 : sdfd sdf \nUser-1 : s fsdf s f\n'),(7,'cookie_id','','ip1','ip2','2010-01-18 04:29:57','0000-00-00 00:00:00','User-2 : hello \'\nUser-2 : or \' and \nUser-2 : or and \' 1=1 -- \' \nUser-1 : hhee \' !@#$%^&*()_ =-\n'),(8,'cookie_id','','ip1','ip2','2010-01-18 00:00:00','0000-00-00 00:00:00','User-2 : ssd\nUser-1 : sdsdsd\n'),(9,'Wp5JBqswTSrdZmiZO73PmY7vuEFoHjq8UjX0uDxMqUCYiKY36JtehbtJXf66wSHNLn18haHwbDiIonsoR8PUPoIDsN1nGKRo3k4l','5Z6V79yylYOMOIMhC4cTy5TzDon0QfONpxFcsesa7wdYJzgtrybGlmqNoq3i4TBXJDUDPbe76R0RTfl9QBbJ4AddFs7k0MWmaRIv','127.0.0.1','127.0.0.1','2010-01-18 05:16:34','2010-01-18 05:16:36','User-2 : sd fdsf\nUser-1 : s dsf \n');
/*!40000 ALTER TABLE `chat_transcripts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-01-18  5:21:24
