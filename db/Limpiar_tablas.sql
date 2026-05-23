-- ===========================================
-- 🧹 LIMPIAR TODAS LAS TABLAS DE PlateUp
-- ===========================================
USE plateup;

-- Desactivar restricciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- Vaciamos primero las tablas hijas (las que tienen claves foráneas) y luego las padres,
-- para evitar errores de integridad referencial incluso con FK_CHECKS desactivado
TRUNCATE TABLE AuditLogs;
TRUNCATE TABLE Reports;
TRUNCATE TABLE Notifications;
TRUNCATE TABLE UserChallenges;
TRUNCATE TABLE Challenges;
TRUNCATE TABLE UserAchievements;
TRUNCATE TABLE Achievements;
TRUNCATE TABLE CollectionRecipes;
TRUNCATE TABLE Collections;
TRUNCATE TABLE Comments;
TRUNCATE TABLE Likes;
TRUNCATE TABLE Follows;
TRUNCATE TABLE Media;
TRUNCATE TABLE RecipeUtensils;
TRUNCATE TABLE Utensils;
TRUNCATE TABLE RecipeIngredients;
TRUNCATE TABLE Ingredients;
TRUNCATE TABLE RecipeSteps;
TRUNCATE TABLE Recipes;
TRUNCATE TABLE Users;

-- Reactivar restricciones
SET FOREIGN_KEY_CHECKS = 1;
