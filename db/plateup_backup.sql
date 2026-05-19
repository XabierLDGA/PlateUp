-- MySQL dump 10.13  Distrib 8.4.9, for Win64 (x86_64)
--
-- Host: plateup-mysql-plateup.h.aivencloud.com    Database: defaultdb
-- ------------------------------------------------------
-- Server version	8.4.8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '8671e2fe-536f-11f1-844e-ce0e6819c4b7:1-15,
86837f53-4f97-11f1-9406-d287dac1366c:1-115';

--
-- Table structure for table `Achievements`
--

DROP TABLE IF EXISTS `Achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Achievements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `icon_url` varchar(255) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `points` int DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Achievements`
--

LOCK TABLES `Achievements` WRITE;
/*!40000 ALTER TABLE `Achievements` DISABLE KEYS */;
INSERT INTO `Achievements` VALUES (1,'Tu primera receta','Publica tu primera receta en PlateUp.','https://picsum.photos/50?achievement_recipe_1','recipes',50,'2026-05-14 13:47:44'),(2,'Chef constante','Publica 10 recetas.','https://picsum.photos/50?achievement_recipe_2','recipes',100,'2026-05-14 13:47:44'),(3,'Maestro de la cocina','Publica 25 recetas.','https://picsum.photos/50?achievement_recipe_3','recipes',200,'2026-05-14 13:47:44'),(4,'Primer aplauso','Recibe tu primer like en una de tus recetas.','https://picsum.photos/50?achievement_like_1','likes',40,'2026-05-14 13:47:44'),(5,'Receta popular','Recibe 10 likes en total en tus recetas.','https://picsum.photos/50?achievement_like_2','likes',100,'2026-05-14 13:47:44'),(6,'Fenómeno foodie','Recibe 25 likes en total en tus recetas.','https://picsum.photos/50?achievement_like_3','likes',180,'2026-05-14 13:47:44'),(7,'Primera receta cocinada','Termina de cocinar tu primera receta.','https://picsum.photos/50?achievement_cooked_1','cooked',50,'2026-05-14 13:47:44'),(8,'Cocinillas','Termina de cocinar 10 recetas.','https://picsum.photos/50?achievement_cooked_2','cooked',110,'2026-05-14 13:47:44'),(9,'Chef imparable','Termina de cocinar 25 recetas.','https://picsum.photos/50?achievement_cooked_3','cooked',220,'2026-05-14 13:47:44'),(10,'Tu primer seguidor','Consigue tu primer seguidor.','https://picsum.photos/50?achievement_followers_1','followers',60,'2026-05-14 13:47:44'),(11,'Creador con comunidad','Consigue 10 seguidores.','https://picsum.photos/50?achievement_followers_2','followers',130,'2026-05-14 13:47:44'),(12,'Estrella de PlateUp','Consigue 25 seguidores.','https://picsum.photos/50?achievement_followers_3','followers',250,'2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Achievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AuditLogs`
--

DROP TABLE IF EXISTS `AuditLogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AuditLogs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actor_user_id` bigint DEFAULT NULL,
  `entity_type` varchar(50) DEFAULT NULL,
  `entity_id` bigint DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_auditlogs_actor` (`actor_user_id`),
  CONSTRAINT `fk_auditlogs_actor` FOREIGN KEY (`actor_user_id`) REFERENCES `Users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AuditLogs`
--

LOCK TABLES `AuditLogs` WRITE;
/*!40000 ALTER TABLE `AuditLogs` DISABLE KEYS */;
/*!40000 ALTER TABLE `AuditLogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Challenges`
--

DROP TABLE IF EXISTS `Challenges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Challenges` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `goal` varchar(100) DEFAULT NULL,
  `reward_points` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Challenges`
--

LOCK TABLES `Challenges` WRITE;
/*!40000 ALTER TABLE `Challenges` DISABLE KEYS */;
INSERT INTO `Challenges` VALUES (1,'Reto vegetariano','Publica 3 recetas sin carne','2025-10-01','2025-10-31','3 recetas',150),(2,'Semana del postre','Prepara un postre cada día','2025-11-01','2025-11-07','7 recetas',200),(3,'Sin fritos','Crea 5 recetas sin freír','2025-10-01','2025-10-31','5 recetas',100);
/*!40000 ALTER TABLE `Challenges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CollectionRecipes`
--

DROP TABLE IF EXISTS `CollectionRecipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CollectionRecipes` (
  `collection_id` bigint NOT NULL,
  `recipe_id` bigint NOT NULL,
  `order_index` int DEFAULT NULL,
  PRIMARY KEY (`collection_id`,`recipe_id`),
  KEY `fk_collectionrecipes_recipe` (`recipe_id`),
  CONSTRAINT `fk_collectionrecipes_collection` FOREIGN KEY (`collection_id`) REFERENCES `Collections` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_collectionrecipes_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CollectionRecipes`
--

LOCK TABLES `CollectionRecipes` WRITE;
/*!40000 ALTER TABLE `CollectionRecipes` DISABLE KEYS */;
/*!40000 ALTER TABLE `CollectionRecipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Collections`
--

DROP TABLE IF EXISTS `Collections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Collections` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `is_public` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_collections_user` (`user_id`),
  CONSTRAINT `fk_collections_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Collections`
--

LOCK TABLES `Collections` WRITE;
/*!40000 ALTER TABLE `Collections` DISABLE KEYS */;
/*!40000 ALTER TABLE `Collections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comments`
--

DROP TABLE IF EXISTS `Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `recipe_id` bigint NOT NULL,
  `text` text NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_comments_user` (`user_id`),
  KEY `idx_comment_recipe` (`recipe_id`),
  CONSTRAINT `fk_comments_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comments_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comments`
--

LOCK TABLES `Comments` WRITE;
/*!40000 ALTER TABLE `Comments` DISABLE KEYS */;
INSERT INTO `Comments` VALUES (1,3,1,'¡Probé esta pasta y está increíble!','2026-05-14 13:47:44'),(2,1,2,'El tiramisú está buenísimo.','2026-05-14 13:47:44'),(3,2,3,'Buena idea para algo rápido.','2026-05-14 13:47:44'),(4,4,4,'Tofu con curry = felicidad.','2026-05-14 13:47:44'),(5,5,5,'Brownie de locura, literal.','2026-05-14 13:47:44'),(6,6,6,'Las croquetas han quedado brutales.','2026-05-14 13:47:44'),(7,7,7,'Salsa brava de verdad, sí señor.','2026-05-14 13:47:44'),(8,8,8,'Muy fresca para el verano.','2026-05-14 13:47:44'),(9,9,9,'Bocadillo top para salir al monte.','2026-05-14 13:47:44'),(10,10,10,'Pan fácil y queda genial.','2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CookedRecipes`
--

DROP TABLE IF EXISTS `CookedRecipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CookedRecipes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `recipe_id` bigint NOT NULL,
  `cooked_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `elapsed_seconds` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cooked` (`user_id`,`recipe_id`),
  KEY `fk_cooked_recipe` (`recipe_id`),
  KEY `idx_cooked_user` (`user_id`),
  CONSTRAINT `fk_cooked_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cooked_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CookedRecipes`
--

LOCK TABLES `CookedRecipes` WRITE;
/*!40000 ALTER TABLE `CookedRecipes` DISABLE KEYS */;
/*!40000 ALTER TABLE `CookedRecipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Follows`
--

DROP TABLE IF EXISTS `Follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Follows` (
  `follower_id` bigint NOT NULL,
  `followed_id` bigint NOT NULL,
  `status` enum('pending','accepted') NOT NULL DEFAULT 'accepted',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`follower_id`,`followed_id`),
  KEY `fk_follows_followed` (`followed_id`),
  CONSTRAINT `fk_follows_followed` FOREIGN KEY (`followed_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_follows_follower` FOREIGN KEY (`follower_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Follows`
--

LOCK TABLES `Follows` WRITE;
/*!40000 ALTER TABLE `Follows` DISABLE KEYS */;
INSERT INTO `Follows` VALUES (1,2,'accepted','2026-05-14 13:47:44'),(1,4,'accepted','2026-05-15 10:46:32'),(1,5,'accepted','2026-05-15 10:46:33'),(1,6,'accepted','2026-05-14 15:01:42'),(1,7,'accepted','2026-05-15 10:46:37'),(1,9,'accepted','2026-05-15 10:46:47'),(1,10,'accepted','2026-05-15 10:46:55'),(3,1,'accepted','2026-05-14 13:47:44'),(3,2,'accepted','2026-05-14 13:47:44'),(4,1,'accepted','2026-05-14 13:47:44'),(5,2,'accepted','2026-05-14 13:47:44'),(6,3,'accepted','2026-05-14 13:47:44'),(7,1,'accepted','2026-05-14 13:47:44'),(8,4,'accepted','2026-05-14 13:47:44'),(9,5,'accepted','2026-05-14 13:47:44'),(10,6,'accepted','2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ingredients`
--

DROP TABLE IF EXISTS `Ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Ingredients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `unit_default` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ingredients`
--

LOCK TABLES `Ingredients` WRITE;
/*!40000 ALTER TABLE `Ingredients` DISABLE KEYS */;
INSERT INTO `Ingredients` VALUES (1,'pasta','g'),(2,'albahaca','g'),(3,'piñones','g'),(4,'queso parmesano','g'),(5,'aceite de oliva','ml'),(6,'arroz','g'),(7,'verduras variadas','g'),(8,'mascarpone','g'),(9,'azúcar','g'),(10,'bizcochos de soletilla','unidad'),(11,'café','ml'),(12,'tofu','g'),(13,'leche de coco','ml'),(14,'pollo','g'),(15,'patata','g'),(16,'salsa brava','ml'),(17,'lechuga','g'),(18,'limón','unidad'),(19,'harina','g'),(20,'levadura','g'),(21,'huevo','unidad'),(22,'pan','unidad'),(23,'mantequilla','g'),(24,'agua','ml'),(25,'sal','g');
/*!40000 ALTER TABLE `Ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Likes`
--

DROP TABLE IF EXISTS `Likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Likes` (
  `user_id` bigint NOT NULL,
  `recipe_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`recipe_id`),
  KEY `idx_like_recipe` (`recipe_id`),
  CONSTRAINT `fk_likes_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_likes_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Likes`
--

LOCK TABLES `Likes` WRITE;
/*!40000 ALTER TABLE `Likes` DISABLE KEYS */;
INSERT INTO `Likes` VALUES (1,2,'2026-05-14 13:47:44'),(1,3,'2026-05-15 10:45:17'),(1,7,'2026-05-14 13:47:44'),(1,10,'2026-05-15 10:46:50'),(2,1,'2026-05-14 13:47:44'),(2,8,'2026-05-14 13:47:44'),(3,1,'2026-05-14 13:47:44'),(3,2,'2026-05-14 13:47:44'),(3,9,'2026-05-14 13:47:44'),(4,5,'2026-05-14 13:47:44'),(4,10,'2026-05-14 13:47:44'),(5,1,'2026-05-14 13:47:44'),(6,3,'2026-05-14 13:47:44'),(7,4,'2026-05-14 13:47:44'),(8,2,'2026-05-14 13:47:44'),(9,5,'2026-05-14 13:47:44'),(10,6,'2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Media`
--

DROP TABLE IF EXISTS `Media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Media` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `owner_user_id` bigint NOT NULL,
  `recipe_id` bigint DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `type` enum('image','video') DEFAULT 'image',
  `order_index` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_media_owner` (`owner_user_id`),
  KEY `idx_media_recipe` (`recipe_id`),
  CONSTRAINT `fk_media_owner` FOREIGN KEY (`owner_user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_media_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Media`
--

LOCK TABLES `Media` WRITE;
/*!40000 ALTER TABLE `Media` DISABLE KEYS */;
INSERT INTO `Media` VALUES (1,1,1,'https://picsum.photos/200?food1','image',1,'2026-05-14 13:47:44'),(2,2,2,'https://picsum.photos/200?food2','image',1,'2026-05-14 13:47:44'),(3,3,3,'https://picsum.photos/200?food3','image',1,'2026-05-14 13:47:44'),(4,4,4,'https://picsum.photos/200?food4','image',1,'2026-05-14 13:47:44'),(5,5,5,'https://picsum.photos/200?food5','image',1,'2026-05-14 13:47:44'),(6,6,6,'https://picsum.photos/200?food6','image',1,'2026-05-14 13:47:44'),(7,7,7,'https://picsum.photos/200?food7','image',1,'2026-05-14 13:47:44'),(8,8,8,'https://picsum.photos/200?food8','image',1,'2026-05-14 13:47:44'),(9,9,9,'https://picsum.photos/200?food9','image',1,'2026-05-14 13:47:44'),(10,10,10,'https://picsum.photos/200?food10','image',1,'2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notifications`
--

DROP TABLE IF EXISTS `Notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recipient_user_id` bigint NOT NULL,
  `actor_user_id` bigint DEFAULT NULL,
  `recipe_id` bigint DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `read_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_notifications_recipient` (`recipient_user_id`),
  KEY `fk_notifications_actor` (`actor_user_id`),
  KEY `fk_notifications_recipe` (`recipe_id`),
  CONSTRAINT `fk_notifications_actor` FOREIGN KEY (`actor_user_id`) REFERENCES `Users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_notifications_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_notifications_recipient` FOREIGN KEY (`recipient_user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notifications`
--

LOCK TABLES `Notifications` WRITE;
/*!40000 ALTER TABLE `Notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `Notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RecipeIngredients`
--

DROP TABLE IF EXISTS `RecipeIngredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RecipeIngredients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recipe_id` bigint NOT NULL,
  `ingredient_id` bigint NOT NULL,
  `quantity_decimal` decimal(10,2) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`id`),
  KEY `idx_recipeingredient_recipe` (`recipe_id`),
  KEY `idx_recipeingredient_ingredient` (`ingredient_id`),
  CONSTRAINT `fk_recipeingredients_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `Ingredients` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipeingredients_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecipeIngredients`
--

LOCK TABLES `RecipeIngredients` WRITE;
/*!40000 ALTER TABLE `RecipeIngredients` DISABLE KEYS */;
INSERT INTO `RecipeIngredients` VALUES (1,1,1,200.00,'g','pasta corta o larga'),(2,1,2,20.00,'g',NULL),(3,1,3,10.00,'g',NULL),(4,1,4,30.00,'g',NULL),(5,1,5,30.00,'ml','aceite extra virgen'),(6,1,25,5.00,'g','para cocer la pasta'),(7,2,8,250.00,'g',NULL),(8,2,9,100.00,'g',NULL),(9,2,10,12.00,'unidad',NULL),(10,2,11,50.00,'ml','café expreso'),(11,2,21,3.00,'unidad','separar yemas y claras'),(12,3,6,150.00,'g',NULL),(13,3,7,100.00,'g','puede ser calabacín, zanahoria o guisantes'),(14,3,5,20.00,'ml',NULL),(15,3,24,300.00,'ml','para la cocción'),(16,3,25,4.00,'g',NULL),(17,4,12,200.00,'g',NULL),(18,4,13,150.00,'ml',NULL),(19,4,5,15.00,'ml',NULL),(20,4,25,3.00,'g',NULL),(21,5,9,100.00,'g',NULL),(22,5,19,80.00,'g','harina de repostería'),(23,5,23,120.00,'g',NULL),(24,5,21,2.00,'unidad',NULL),(25,6,14,250.00,'g','pollo cocido o asado'),(26,6,19,80.00,'g','para la bechamel'),(27,6,23,60.00,'g','para la bechamel'),(28,6,24,500.00,'ml','para la bechamel'),(29,6,25,5.00,'g',NULL),(30,7,15,500.00,'g',NULL),(31,7,16,120.00,'ml',NULL),(32,7,5,20.00,'ml',NULL),(33,7,25,4.00,'g',NULL),(34,8,17,150.00,'g',NULL),(35,8,18,1.00,'unidad',NULL),(36,8,5,20.00,'ml','para aliñar'),(37,8,25,2.00,'g',NULL),(38,9,15,200.00,'g',NULL),(39,9,21,3.00,'unidad',NULL),(40,9,22,1.00,'unidad','barra pequeña o bocadillo'),(41,9,25,3.00,'g',NULL),(42,10,19,500.00,'g',NULL),(43,10,20,7.00,'g','levadura seca'),(44,10,24,325.00,'ml','agua templada'),(45,10,25,10.00,'g',NULL);
/*!40000 ALTER TABLE `RecipeIngredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RecipeSteps`
--

DROP TABLE IF EXISTS `RecipeSteps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RecipeSteps` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recipe_id` bigint NOT NULL,
  `order_index` int NOT NULL,
  `text` text NOT NULL,
  `timer_seconds` int DEFAULT NULL,
  `media_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_step_recipe` (`recipe_id`),
  CONSTRAINT `fk_recipesteps_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecipeSteps`
--

LOCK TABLES `RecipeSteps` WRITE;
/*!40000 ALTER TABLE `RecipeSteps` DISABLE KEYS */;
INSERT INTO `RecipeSteps` VALUES (1,1,1,'Hierve la pasta en agua con sal durante 8 minutos.',480,NULL),(2,1,2,'Tritura la albahaca, piñones, queso y aceite.',NULL,NULL),(3,1,3,'Mezcla con la pasta cocida y sirve.',NULL,NULL),(4,2,1,'Bate las yemas con azúcar y añade el mascarpone.',NULL,NULL),(5,2,2,'Monta las claras a punto de nieve.',NULL,NULL),(6,2,3,'Monta el postre en capas con bizcochos y café.',NULL,NULL),(7,3,1,'Corta las verduras en trozos pequeños.',NULL,NULL),(8,3,2,'Sofríelas con aceite y añade el arroz.',NULL,NULL),(9,3,3,'Cubre con agua y deja cocer 15 minutos.',900,NULL),(10,4,1,'Saltea tofu con aceite.',NULL,NULL),(11,4,2,'Añade curry y leche de coco.',NULL,NULL),(12,4,3,'Cuece 10 minutos.',600,NULL),(13,5,1,'Funde mantequilla y chocolate.',NULL,NULL),(14,5,2,'Añade azúcar, huevos y harina.',NULL,NULL),(15,5,3,'Hornea 25 minutos.',1500,NULL),(16,6,1,'Cuece el pollo y pícalo muy fino.',NULL,NULL),(17,6,2,'Prepara una bechamel espesa y mézclala con el pollo.',NULL,NULL),(18,6,3,'Forma las croquetas, rebózalas y fríelas hasta dorarlas.',NULL,NULL),(19,7,1,'Corta las patatas en cubos y sécalas bien.',NULL,NULL),(20,7,2,'Fríelas o hornéalas hasta que queden crujientes.',NULL,NULL),(21,7,3,'Sirve con salsa brava por encima.',NULL,NULL),(22,8,1,'Lava y corta la lechuga y los vegetales.',NULL,NULL),(23,8,2,'Añade limón y aceite para aliñar.',NULL,NULL),(24,8,3,'Mezcla todo justo antes de servir.',NULL,NULL),(25,9,1,'Pela y corta la patata en láminas finas.',NULL,NULL),(26,9,2,'Prepara la tortilla con huevo y patata.',NULL,NULL),(27,9,3,'Monta el bocadillo con pan crujiente.',NULL,NULL),(28,10,1,'Mezcla harina, agua, sal y levadura.',NULL,NULL),(29,10,2,'Deja reposar la masa hasta que crezca.',3600,NULL),(30,10,3,'Hornea hasta que el pan esté dorado.',2700,NULL);
/*!40000 ALTER TABLE `RecipeSteps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RecipeUtensils`
--

DROP TABLE IF EXISTS `RecipeUtensils`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RecipeUtensils` (
  `recipe_id` bigint NOT NULL,
  `utensil_id` bigint NOT NULL,
  PRIMARY KEY (`recipe_id`,`utensil_id`),
  KEY `fk_recipeutensils_utensil` (`utensil_id`),
  CONSTRAINT `fk_recipeutensils_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipeutensils_utensil` FOREIGN KEY (`utensil_id`) REFERENCES `Utensils` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecipeUtensils`
--

LOCK TABLES `RecipeUtensils` WRITE;
/*!40000 ALTER TABLE `RecipeUtensils` DISABLE KEYS */;
INSERT INTO `RecipeUtensils` VALUES (1,1),(3,1),(6,1),(3,2),(4,2),(6,2),(7,2),(9,2),(1,3),(2,3),(2,4),(5,4),(10,4),(5,5),(6,5),(1,6),(4,8),(10,8),(7,9),(8,9),(8,10),(9,10);
/*!40000 ALTER TABLE `RecipeUtensils` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Recipes`
--

DROP TABLE IF EXISTS `Recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Recipes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(150) NOT NULL,
  `description` text,
  `category` varchar(50) NOT NULL DEFAULT 'Quick',
  `image_url` varchar(500) DEFAULT NULL,
  `servings` int DEFAULT NULL,
  `total_minutes` int DEFAULT NULL,
  `difficulty` enum('easy','medium','hard') DEFAULT 'medium',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_recipe_user` (`user_id`),
  CONSTRAINT `fk_recipes_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Recipes`
--

LOCK TABLES `Recipes` WRITE;
/*!40000 ALTER TABLE `Recipes` DISABLE KEYS */;
INSERT INTO `Recipes` VALUES (1,1,'Pasta al pesto','Receta clásica italiana con albahaca y piñones.','Pasta','https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1200&q=80',2,25,'easy','2026-03-17 13:24:00','2026-05-14 13:47:44'),(2,2,'Tiramisú casero','Postre italiano con mascarpone y café.','Dessert','https://images.unsplash.com/photo-1512058564366-18510be2db19?auto=format&fit=crop&w=1200&q=80',6,40,'medium','2026-03-18 20:11:00','2026-05-14 13:47:44'),(3,3,'Arroz con verduras','Rápido, barato y perfecto para estudiantes.','Rice','https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=1200&q=80',2,20,'easy','2026-03-19 09:42:00','2026-05-14 13:47:44'),(4,4,'Tofu al curry','Sabor potente y cero carne.','Vegan','https://images.unsplash.com/photo-1482049016688-2d3e1b311543?auto=format&fit=crop&w=1200&q=80',3,30,'medium','2026-03-20 14:37:00','2026-05-14 13:47:44'),(5,5,'Brownie loco','Mucho chocolate y poca vergüenza.','Dessert','https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=80',8,35,'medium','2026-03-21 18:56:00','2026-05-14 13:47:44'),(6,6,'Croquetas de pollo','Receta de la abuela, infalible.','Meat','https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=80',6,70,'hard','2026-03-22 12:08:00','2026-05-14 13:47:44'),(7,7,'Patatas bravas crujientes','Con salsa de verdad, no ketchup.','Snack','https://images.unsplash.com/photo-1529042410759-befb1204b468?auto=format&fit=crop&w=1200&q=80',4,25,'easy','2026-03-22 19:21:00','2026-05-14 13:47:44'),(8,8,'Ensalada detox','Verde, ligera y con chispa.','Salad','https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1200&q=80',2,10,'easy','2026-03-23 08:17:00','2026-05-14 13:47:44'),(9,9,'Bocadillo de tortilla','Ideal para excursiones.','Student','https://images.unsplash.com/photo-1539252554453-80ab65ce3586?auto=format&fit=crop&w=1200&q=80',1,15,'easy','2026-03-23 10:03:00','2026-05-14 13:47:44'),(10,10,'Pan casero rápido','Hecho sin amasado, fácil y rico.','Quick','https://images.unsplash.com/photo-1608198093002-ad4e005484ec?auto=format&fit=crop&w=1200&q=80',8,180,'medium','2026-03-23 11:26:00','2026-05-14 13:47:44');
/*!40000 ALTER TABLE `Recipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reports`
--

DROP TABLE IF EXISTS `Reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Reports` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reporter_user_id` bigint NOT NULL,
  `target_user_id` bigint DEFAULT NULL,
  `recipe_id` bigint DEFAULT NULL,
  `reason` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_reports_reporter` (`reporter_user_id`),
  KEY `fk_reports_target` (`target_user_id`),
  KEY `fk_reports_recipe` (`recipe_id`),
  CONSTRAINT `fk_reports_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `Recipes` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_reports_reporter` FOREIGN KEY (`reporter_user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reports_target` FOREIGN KEY (`target_user_id`) REFERENCES `Users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reports`
--

LOCK TABLES `Reports` WRITE;
/*!40000 ALTER TABLE `Reports` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserAchievements`
--

DROP TABLE IF EXISTS `UserAchievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UserAchievements` (
  `user_id` bigint NOT NULL,
  `achievement_id` bigint NOT NULL,
  `earned_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `level` int DEFAULT '1',
  PRIMARY KEY (`user_id`,`achievement_id`),
  KEY `fk_userachievements_achievement` (`achievement_id`),
  CONSTRAINT `fk_userachievements_achievement` FOREIGN KEY (`achievement_id`) REFERENCES `Achievements` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_userachievements_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserAchievements`
--

LOCK TABLES `UserAchievements` WRITE;
/*!40000 ALTER TABLE `UserAchievements` DISABLE KEYS */;
INSERT INTO `UserAchievements` VALUES (1,1,'2026-05-14 14:59:42',1),(1,4,'2026-05-14 14:59:45',1),(1,10,'2026-05-14 14:59:46',1),(2,1,'2026-05-14 14:59:47',1),(2,4,'2026-05-14 14:59:48',1),(2,10,'2026-05-14 14:59:50',1),(3,1,'2026-05-14 14:59:51',1),(3,4,'2026-05-14 14:59:52',1),(3,10,'2026-05-14 14:59:53',1),(4,1,'2026-05-14 14:59:54',1),(4,4,'2026-05-14 14:59:54',1),(4,10,'2026-05-14 14:59:56',1),(5,1,'2026-05-14 14:59:57',1),(5,4,'2026-05-14 14:59:57',1),(5,10,'2026-05-14 14:59:59',1),(6,1,'2026-05-14 15:00:00',1),(6,4,'2026-05-14 15:00:00',1),(6,10,'2026-05-14 15:00:02',1),(7,1,'2026-05-14 15:00:02',1),(7,4,'2026-05-14 15:00:03',1),(7,10,'2026-05-15 10:46:32',1),(8,1,'2026-05-14 15:00:05',1),(8,4,'2026-05-14 15:00:06',1),(9,1,'2026-05-14 15:00:08',1),(9,4,'2026-05-14 15:00:09',1),(9,10,'2026-05-15 10:46:42',1),(10,1,'2026-05-14 15:00:11',1),(10,4,'2026-05-14 15:00:11',1),(10,10,'2026-05-15 10:46:50',1);
/*!40000 ALTER TABLE `UserAchievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserChallenges`
--

DROP TABLE IF EXISTS `UserChallenges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UserChallenges` (
  `user_id` bigint NOT NULL,
  `challenge_id` bigint NOT NULL,
  `progress` decimal(5,2) DEFAULT '0.00',
  `status` enum('active','completed','failed') DEFAULT 'active',
  `started_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `finished_at` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`challenge_id`),
  KEY `fk_userchallenges_challenge` (`challenge_id`),
  CONSTRAINT `fk_userchallenges_challenge` FOREIGN KEY (`challenge_id`) REFERENCES `Challenges` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_userchallenges_user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserChallenges`
--

LOCK TABLES `UserChallenges` WRITE;
/*!40000 ALTER TABLE `UserChallenges` DISABLE KEYS */;
INSERT INTO `UserChallenges` VALUES (1,1,0.60,'active','2026-05-14 13:47:45',NULL),(2,2,1.00,'completed','2026-05-14 13:47:45',NULL),(3,3,0.30,'active','2026-05-14 13:47:45',NULL);
/*!40000 ALTER TABLE `UserChallenges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `display_name` varchar(100) DEFAULT NULL,
  `bio` text,
  `avatar_url` varchar(255) DEFAULT NULL,
  `visibility_default` enum('private','followers','public') DEFAULT 'public',
  `streak_count` int DEFAULT '0',
  `last_active_date` date DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role` varchar(10) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'camaron','camaron@example.com','$2a$10$7WgfJY1NwUapKZWW88xDP.CQ2j5C6ZhS0dVzqw46UNktyR./odZMe','Camarón Mantis','Soy un camarón mantis.','https://media.istockphoto.com/id/2192691445/es/foto/odontodactylus-scyllarus.jpg?s=612x612&w=0&k=20&c=Taq6oFbSVoMMeaBY5EqgCtWWDrSw04pyegDLvCJcKNE=','public',2,'2026-05-15','2026-05-14 13:47:44','2026-05-15 10:39:37','ADMIN'),(2,'saratgn','sara@example.com','saratgn','Sara Teglas','La mujer de Xabier López de Guereño Armada.','https://media.licdn.com/dms/image/v2/D5603AQGFtHcFicA-sg/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1719503696706?e=2147483647&v=beta&t=QHPtg3yGspCyvgAt7twdv0_ZYkmIpoIn-1W0So6BGFY','followers',0,NULL,'2026-05-14 13:47:44','2026-05-15 10:45:41','USER'),(3,'miguelbeltran','miguelbeltran@example.com','miguelbeltran','Miguel Beltrán','Se me ha puesto como una cachiporra.','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Tamias_striatus_CT.jpg/500px-Tamias_striatus_CT.jpg','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 15:03:51','USER'),(4,'samuelhdez','samuel@example.com','samuelhdez','Samuel Hernández','El piranya.','https://pbs.twimg.com/media/E6buFZ5WUAcXkjm.jpg','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(5,'davidgimenez','david@example.com','davidgimenez','David Giménez','Erika, vuelve conmigo por favor.','https://yt3.googleusercontent.com/UZsSMbWFhV4DAa66jLUOW4emrwYayCkHyM8VNBQSYCXRCR5GEZjC-2i2kX90xUIj1ihttsFakQM=s160-c-k-c0x00ffffff-no-rj','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(6,'pablogallego','pablogallego@example.com','pablogallego','Pablo Gallego','Cocina y buenas vibes (cocina gallega de la buena).','https://st3.depositphotos.com/1967477/31855/v/450/depositphotos_318559454-stock-illustration-vector-illustration-cute-octopus-cartoon.jpg','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(7,'josebenjamin','jose@example.com','josebenjamin','Jose Benjamín','Cenar a las 19:00, hacer un crucigrama y a dormir.','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5pVwRXMnROJ0kU6Ls0jb9XBgKqMobMfQqrQ&s','followers',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(8,'lucasgabaldon','lucas@example.com','lucasgabaldon','Lucas Gabaldon','Con la bici por ahí.','https://media.gettyimages.com/id/2117342420/es/foto/portrait-of-homosexual-man-riding-a-bicycle-outdoors.jpg?s=612x612&w=gi&k=20&c=AfraV2DK6qtN-QI_sAuYztpvKWQRzxQrGXA47EBBa8M=','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(9,'guillermomartin','guillermo@example.com','guillermomartin','Guillermo Martin','Yo hago el amor.','https://www.disfracessimon.com/cdn/shop/files/7068_a.jpg?v=1751399681','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(10,'dariopaez','dario@example.com','dariopaez','Dario Paez','Powerlifter de éxito exitosamente exitoso.','https://i.pinimg.com/originals/47/a0/34/47a0343d17b41b9298f31a3e7cb64245.jpg','public',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER'),(11,'pablogonzalez','pablogonzalez@example.com','pablogonzalez','Pablo Gonzalez','Estoy forrao.','https://static9.depositphotos.com/1594308/1124/i/450/depositphotos_11241524-stock-photo-wealthy-boy.jpg','followers',0,NULL,'2026-05-14 13:47:44','2026-05-14 13:47:44','USER');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utensils`
--

DROP TABLE IF EXISTS `Utensils`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utensils` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utensils`
--

LOCK TABLES `Utensils` WRITE;
/*!40000 ALTER TABLE `Utensils` DISABLE KEYS */;
INSERT INTO `Utensils` VALUES (3,'batidora'),(8,'cazo'),(6,'colador'),(10,'cuchillo'),(5,'espátula'),(4,'horno'),(1,'olla'),(7,'rallador'),(2,'sartén'),(9,'tabla de cortar');
/*!40000 ALTER TABLE `Utensils` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-19 12:57:35
