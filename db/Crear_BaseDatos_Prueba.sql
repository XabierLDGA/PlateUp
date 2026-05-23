-- ===========================================
-- Base de datos: PlateUp
-- Script corregido y consistente con backend/frontend
-- ===========================================

-- Recreamos la base de datos desde cero con soporte completo para emojis y caracteres especiales
DROP DATABASE IF EXISTS plateup;
CREATE DATABASE plateup CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE plateup;

-- ===========================================
-- USERS
-- ===========================================
-- Tabla de usuarios: almacena el perfil y credenciales de cada cuenta registrada
-- La visibilidad controla quién puede ver el perfil (privado, solo seguidores, o público)
CREATE TABLE Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    bio TEXT,
    avatar_url VARCHAR(255),
    visibility_default ENUM('private','followers','public') DEFAULT 'public',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ===========================================
-- RECIPES
-- ===========================================
CREATE TABLE Recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    servings INT,
    total_minutes INT,
    difficulty ENUM('easy','medium','hard') DEFAULT 'medium',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipes_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- ===========================================
-- RECIPE STEPS
-- ===========================================
-- Pasos de la receta ordenados por order_index; pueden incluir un temporizador y una imagen de apoyo
CREATE TABLE RecipeSteps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    order_index INT NOT NULL,
    text TEXT NOT NULL,
    timer_seconds INT,
    media_url VARCHAR(255),
    CONSTRAINT fk_recipesteps_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- INGREDIENTS
-- ===========================================
CREATE TABLE Ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_default VARCHAR(20)
);

-- ===========================================
-- RECIPE INGREDIENTS
-- ===========================================
CREATE TABLE RecipeIngredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity_decimal DECIMAL(10,2),
    unit VARCHAR(20),
    notes TEXT,
    CONSTRAINT fk_recipeingredients_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipeingredients_ingredient FOREIGN KEY (ingredient_id) REFERENCES Ingredients(id) ON DELETE CASCADE
);

-- ===========================================
-- UTENSILS
-- ===========================================
CREATE TABLE Utensils (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- ===========================================
-- RECIPE UTENSILS
-- ===========================================
CREATE TABLE RecipeUtensils (
    recipe_id BIGINT NOT NULL,
    utensil_id BIGINT NOT NULL,
    PRIMARY KEY (recipe_id, utensil_id),
    CONSTRAINT fk_recipeutensils_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipeutensils_utensil FOREIGN KEY (utensil_id) REFERENCES Utensils(id) ON DELETE CASCADE
);

-- ===========================================
-- MEDIA
-- ===========================================
CREATE TABLE Media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_user_id BIGINT NOT NULL,
    recipe_id BIGINT,
    url VARCHAR(255) NOT NULL,
    type ENUM('image','video') DEFAULT 'image',
    order_index INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_media_owner FOREIGN KEY (owner_user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_media_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- FOLLOWS
-- ===========================================
CREATE TABLE Follows (
    follower_id BIGINT NOT NULL,
    followed_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    CONSTRAINT fk_follows_follower FOREIGN KEY (follower_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_follows_followed FOREIGN KEY (followed_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- ===========================================
-- LIKES
-- ===========================================
CREATE TABLE Likes (
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, recipe_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- COMMENTS
-- ===========================================
CREATE TABLE Comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    text TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- COLLECTIONS
-- ===========================================
CREATE TABLE Collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_collections_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- ===========================================
-- COLLECTION RECIPES
-- ===========================================
CREATE TABLE CollectionRecipes (
    collection_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    order_index INT,
    PRIMARY KEY (collection_id, recipe_id),
    CONSTRAINT fk_collectionrecipes_collection FOREIGN KEY (collection_id) REFERENCES Collections(id) ON DELETE CASCADE,
    CONSTRAINT fk_collectionrecipes_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- ACHIEVEMENTS
-- ===========================================
-- Logros desbloqueables que se otorgan al cumplir ciertos hitos dentro de la app
CREATE TABLE Achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    category VARCHAR(50),
    points INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- USER ACHIEVEMENTS
-- ===========================================
CREATE TABLE UserAchievements (
    user_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    earned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    level INT DEFAULT 1,
    PRIMARY KEY (user_id, achievement_id),
    CONSTRAINT fk_userachievements_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_userachievements_achievement FOREIGN KEY (achievement_id) REFERENCES Achievements(id) ON DELETE CASCADE
);

-- ===========================================
-- CHALLENGES
-- ===========================================
CREATE TABLE Challenges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    goal VARCHAR(100),
    reward_points INT DEFAULT 0
);

-- ===========================================
-- USER CHALLENGES
-- ===========================================
CREATE TABLE UserChallenges (
    user_id BIGINT NOT NULL,
    challenge_id BIGINT NOT NULL,
    progress DECIMAL(5,2) DEFAULT 0,
    status ENUM('active','completed','failed') DEFAULT 'active',
    started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    finished_at DATETIME,
    PRIMARY KEY (user_id, challenge_id),
    CONSTRAINT fk_userchallenges_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_userchallenges_challenge FOREIGN KEY (challenge_id) REFERENCES Challenges(id) ON DELETE CASCADE
);

-- ===========================================
-- NOTIFICATIONS
-- ===========================================
CREATE TABLE Notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipient_user_id BIGINT NOT NULL,
    actor_user_id BIGINT,
    recipe_id BIGINT,
    type VARCHAR(50),
    read_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_recipient FOREIGN KEY (recipient_user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notifications_actor FOREIGN KEY (actor_user_id) REFERENCES Users(id) ON DELETE SET NULL,
    CONSTRAINT fk_notifications_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE SET NULL
);

-- ===========================================
-- REPORTS
-- ===========================================
CREATE TABLE Reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_user_id BIGINT NOT NULL,
    target_user_id BIGINT,
    recipe_id BIGINT,
    reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reports_reporter FOREIGN KEY (reporter_user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reports_target FOREIGN KEY (target_user_id) REFERENCES Users(id) ON DELETE SET NULL,
    CONSTRAINT fk_reports_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE SET NULL
);

-- ===========================================
-- AUDIT LOGS
-- ===========================================
CREATE TABLE AuditLogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    actor_user_id BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    action VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditlogs_actor FOREIGN KEY (actor_user_id) REFERENCES Users(id) ON DELETE SET NULL
);

-- ===========================================
-- ÍNDICES
-- ===========================================
-- Índices para acelerar las consultas más frecuentes de la aplicación
CREATE INDEX idx_recipe_user ON Recipes(user_id);
CREATE INDEX idx_step_recipe ON RecipeSteps(recipe_id);
CREATE INDEX idx_recipeingredient_recipe ON RecipeIngredients(recipe_id);
CREATE INDEX idx_recipeingredient_ingredient ON RecipeIngredients(ingredient_id);
CREATE INDEX idx_comment_recipe ON Comments(recipe_id);
CREATE INDEX idx_like_recipe ON Likes(recipe_id);
CREATE INDEX idx_media_recipe ON Media(recipe_id);