-- ===========================================
-- Base de datos: PlateUp
-- Script corregido y consistente con backend/frontend
-- ===========================================

DROP DATABASE IF EXISTS plateup;
CREATE DATABASE plateup CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE plateup;

-- ===========================================
-- USERS
-- ===========================================
CREATE TABLE Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    bio TEXT,
    avatar_url VARCHAR(255),
    visibility_default ENUM('private','followers','public') DEFAULT 'public',
    streak_count INT DEFAULT 0,
    last_active_date DATE NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    role VARCHAR(10) NOT NULL DEFAULT 'USER'
);

-- ===========================================
-- RECIPES
-- ===========================================
CREATE TABLE Recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL DEFAULT 'Quick',
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
    status ENUM('pending','accepted') NOT NULL DEFAULT 'accepted',
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
-- COOKED RECIPES
-- ===========================================
CREATE TABLE CookedRecipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    cooked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    elapsed_seconds INT DEFAULT 0,
    CONSTRAINT uq_cooked UNIQUE (user_id, recipe_id),
    CONSTRAINT fk_cooked_user FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT fk_cooked_recipe FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE
);

-- ===========================================
-- ÍNDICES
-- ===========================================
CREATE INDEX idx_recipe_user ON Recipes(user_id);
CREATE INDEX idx_step_recipe ON RecipeSteps(recipe_id);
CREATE INDEX idx_recipeingredient_recipe ON RecipeIngredients(recipe_id);
CREATE INDEX idx_recipeingredient_ingredient ON RecipeIngredients(ingredient_id);
CREATE INDEX idx_comment_recipe ON Comments(recipe_id);
CREATE INDEX idx_like_recipe ON Likes(recipe_id);
CREATE INDEX idx_media_recipe ON Media(recipe_id);
CREATE INDEX idx_cooked_user ON CookedRecipes(user_id);

-- ===========================================
-- LIMPIEZA DE DATOS
-- ===========================================
SET FOREIGN_KEY_CHECKS = 0;

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
TRUNCATE TABLE CookedRecipes;
TRUNCATE TABLE Recipes;
TRUNCATE TABLE Users;

SET FOREIGN_KEY_CHECKS = 1;

-- ===========================================
-- USERS
-- ===========================================
INSERT INTO Users (username, email, password_hash, display_name, bio, avatar_url, visibility_default, streak_count, last_active_date)
VALUES
('camaron','camaron@example.com','camaron','Camarón Mantis','Soy un camarón mantis.','https://media.istockphoto.com/id/2192691445/es/foto/odontodactylus-scyllarus.jpg?s=612x612&w=0&k=20&c=Taq6oFbSVoMMeaBY5EqgCtWWDrSw04pyegDLvCJcKNE=','public',7,'2026-04-07'),('saratgn','sara@example.com','saratgn','Sara Teglas','La mujer de Xabier López de Guereño Armada.','https://media.licdn.com/dms/image/v2/D5603AQGFtHcFicA-sg/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1719503696706?e=2147483647&v=beta&t=QHPtg3yGspCyvgAt7twdv0_ZYkmIpoIn-1W0So6BGFY','followers',0,NULL),
('miguelbeltran','miguelbeltran@example.com','miguelbeltran','Miguel Beltrán','Se me ha puesto como una cachiporra.','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Tamias_striatus_CT.jpg/500px-Tamias_striatus_CT.jpg','public',0,NULL),
('samuelhdez','samuel@example.com','samuelhdez','Samuel Hernández','El piranya.','https://pbs.twimg.com/media/E6buFZ5WUAcXkjm.jpg','public',0,NULL),
('davidgimenez','david@example.com','davidgimenez','David Giménez','Erika, vuelve conmigo por favor.','https://yt3.googleusercontent.com/UZsSMbWFhV4DAa66jLUOW4emrwYayCkHyM8VNBQSYCXRCR5GEZjC-2i2kX90xUIj1ihttsFakQM=s160-c-k-c0x00ffffff-no-rj','public',0,NULL),
('pablogallego','pablogallego@example.com','pablogallego','Pablo Gallego','Cocina y buenas vibes (cocina gallega de la buena).','https://st3.depositphotos.com/1967477/31855/v/450/depositphotos_318559454-stock-illustration-vector-illustration-cute-octopus-cartoon.jpg','public',0,NULL),
('josebenjamin','jose@example.com','josebenjamin','Jose Benjamín','Cenar a las 19:00, hacer un crucigrama y a dormir.','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5pVwRXMnROJ0kU6Ls0jb9XBgKqMobMfQqrQ&s','followers',0,NULL),
('lucasgabaldon','lucas@example.com','lucasgabaldon','Lucas Gabaldon','Con la bici por ahí.','https://media.gettyimages.com/id/2117342420/es/foto/portrait-of-homosexual-man-riding-a-bicycle-outdoors.jpg?s=612x612&w=gi&k=20&c=AfraV2DK6qtN-QI_sAuYztpvKWQRzxQrGXA47EBBa8M=','public',0,NULL),
('guillermomartin','guillermo@example.com','guillermomartin','Guillermo Martin','Yo hago el amor.','https://www.disfracessimon.com/cdn/shop/files/7068_a.jpg?v=1751399681','public',0,NULL),
('dariopaez','dario@example.com','dariopaez','Dario Paez','Powerlifter de éxito exitosamente exitoso.','https://i.pinimg.com/originals/47/a0/34/47a0343d17b41b9298f31a3e7cb64245.jpg','public',0,NULL),
('pablogonzalez','pablogonzalez@example.com','pablogonzalez','Pablo Gonzalez','Estoy forrao.','https://static9.depositphotos.com/1594308/1124/i/450/depositphotos_11241524-stock-photo-wealthy-boy.jpg','followers',0,NULL);

-- ===========================================
-- RECIPES
-- ===========================================
INSERT INTO Recipes (user_id, title, description, category, image_url, servings, total_minutes, difficulty, created_at)
VALUES
(1, 'Pasta al pesto', 'Receta clásica italiana con albahaca y piñones.', 'Pasta', 'https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1200&q=80', 2, 25, 'easy', '2026-03-17 13:24:00'),
(2, 'Tiramisú casero', 'Postre italiano con mascarpone y café.', 'Dessert', 'https://images.unsplash.com/photo-1512058564366-18510be2db19?auto=format&fit=crop&w=1200&q=80', 6, 40, 'medium', '2026-03-18 20:11:00'),
(3, 'Arroz con verduras', 'Rápido, barato y perfecto para estudiantes.', 'Rice', 'https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=1200&q=80', 2, 20, 'easy', '2026-03-19 09:42:00'),
(4, 'Tofu al curry', 'Sabor potente y cero carne.', 'Vegan', 'https://images.unsplash.com/photo-1482049016688-2d3e1b311543?auto=format&fit=crop&w=1200&q=80', 3, 30, 'medium', '2026-03-20 14:37:00'),
(5, 'Brownie loco', 'Mucho chocolate y poca vergüenza.', 'Dessert', 'https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=80', 8, 35, 'medium', '2026-03-21 18:56:00'),
(6, 'Croquetas de pollo', 'Receta de la abuela, infalible.', 'Meat', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=80', 6, 70, 'hard', '2026-03-22 12:08:00'),
(7, 'Patatas bravas crujientes', 'Con salsa de verdad, no ketchup.', 'Snack', 'https://images.unsplash.com/photo-1529042410759-befb1204b468?auto=format&fit=crop&w=1200&q=80', 4, 25, 'easy', '2026-03-22 19:21:00'),
(8, 'Ensalada detox', 'Verde, ligera y con chispa.', 'Salad', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1200&q=80', 2, 10, 'easy', '2026-03-23 08:17:00'),
(9, 'Bocadillo de tortilla', 'Ideal para excursiones.', 'Student', 'https://images.unsplash.com/photo-1539252554453-80ab65ce3586?auto=format&fit=crop&w=1200&q=80', 1, 15, 'easy', '2026-03-23 10:03:00'),
(10, 'Pan casero rápido', 'Hecho sin amasado, fácil y rico.', 'Quick', 'https://images.unsplash.com/photo-1608198093002-ad4e005484ec?auto=format&fit=crop&w=1200&q=80', 8, 180, 'medium', '2026-03-23 11:26:00');

-- ===========================================
-- RECIPE STEPS
-- ===========================================
INSERT INTO RecipeSteps (recipe_id, order_index, text, timer_seconds)
VALUES
(1,1,'Hierve la pasta en agua con sal durante 8 minutos.',480),
(1,2,'Tritura la albahaca, piñones, queso y aceite.',NULL),
(1,3,'Mezcla con la pasta cocida y sirve.',NULL),
(2,1,'Bate las yemas con azúcar y añade el mascarpone.',NULL),
(2,2,'Monta las claras a punto de nieve.',NULL),
(2,3,'Monta el postre en capas con bizcochos y café.',NULL),
(3,1,'Corta las verduras en trozos pequeños.',NULL),
(3,2,'Sofríelas con aceite y añade el arroz.',NULL),
(3,3,'Cubre con agua y deja cocer 15 minutos.',900),
(4,1,'Saltea tofu con aceite.',NULL),
(4,2,'Añade curry y leche de coco.',NULL),
(4,3,'Cuece 10 minutos.',600),
(5,1,'Funde mantequilla y chocolate.',NULL),
(5,2,'Añade azúcar, huevos y harina.',NULL),
(5,3,'Hornea 25 minutos.',1500),
(6,1,'Cuece el pollo y pícalo muy fino.',NULL),
(6,2,'Prepara una bechamel espesa y mézclala con el pollo.',NULL),
(6,3,'Forma las croquetas, rebózalas y fríelas hasta dorarlas.',NULL),
(7,1,'Corta las patatas en cubos y sécalas bien.',NULL),
(7,2,'Fríelas o hornéalas hasta que queden crujientes.',NULL),
(7,3,'Sirve con salsa brava por encima.',NULL),
(8,1,'Lava y corta la lechuga y los vegetales.',NULL),
(8,2,'Añade limón y aceite para aliñar.',NULL),
(8,3,'Mezcla todo justo antes de servir.',NULL),
(9,1,'Pela y corta la patata en láminas finas.',NULL),
(9,2,'Prepara la tortilla con huevo y patata.',NULL),
(9,3,'Monta el bocadillo con pan crujiente.',NULL),
(10,1,'Mezcla harina, agua, sal y levadura.',NULL),
(10,2,'Deja reposar la masa hasta que crezca.',3600),
(10,3,'Hornea hasta que el pan esté dorado.',2700);

-- ===========================================
-- INGREDIENTS
-- ===========================================
INSERT INTO Ingredients (name, unit_default)
VALUES
('pasta','g'),
('albahaca','g'),
('piñones','g'),
('queso parmesano','g'),
('aceite de oliva','ml'),
('arroz','g'),
('verduras variadas','g'),
('mascarpone','g'),
('azúcar','g'),
('bizcochos de soletilla','unidad'),
('café','ml'),
('tofu','g'),
('leche de coco','ml'),
('pollo','g'),
('patata','g'),
('salsa brava','ml'),
('lechuga','g'),
('limón','unidad'),
('harina','g'),
('levadura','g'),
('huevo','unidad'),
('pan','unidad'),
('mantequilla','g'),
('agua','ml'),
('sal','g');

-- ===========================================
-- RECIPE INGREDIENTS
-- ===========================================
INSERT INTO RecipeIngredients (recipe_id, ingredient_id, quantity_decimal, unit, notes)
VALUES
(1,1,200.00,'g','pasta corta o larga'),
(1,2,20.00,'g',NULL),
(1,3,10.00,'g',NULL),
(1,4,30.00,'g',NULL),
(1,5,30.00,'ml','aceite extra virgen'),
(1,25,5.00,'g','para cocer la pasta'),
(2,8,250.00,'g',NULL),
(2,9,100.00,'g',NULL),
(2,10,12.00,'unidad',NULL),
(2,11,50.00,'ml','café expreso'),
(2,21,3.00,'unidad','separar yemas y claras'),
(3,6,150.00,'g',NULL),
(3,7,100.00,'g','puede ser calabacín, zanahoria o guisantes'),
(3,5,20.00,'ml',NULL),
(3,24,300.00,'ml','para la cocción'),
(3,25,4.00,'g',NULL),
(4,12,200.00,'g',NULL),
(4,13,150.00,'ml',NULL),
(4,5,15.00,'ml',NULL),
(4,25,3.00,'g',NULL),
(5,9,100.00,'g',NULL),
(5,19,80.00,'g','harina de repostería'),
(5,23,120.00,'g',NULL),
(5,21,2.00,'unidad',NULL),
(6,14,250.00,'g','pollo cocido o asado'),
(6,19,80.00,'g','para la bechamel'),
(6,23,60.00,'g','para la bechamel'),
(6,24,500.00,'ml','para la bechamel'),
(6,25,5.00,'g',NULL),
(7,15,500.00,'g',NULL),
(7,16,120.00,'ml',NULL),
(7,5,20.00,'ml',NULL),
(7,25,4.00,'g',NULL),
(8,17,150.00,'g',NULL),
(8,18,1.00,'unidad',NULL),
(8,5,20.00,'ml','para aliñar'),
(8,25,2.00,'g',NULL),
(9,15,200.00,'g',NULL),
(9,21,3.00,'unidad',NULL),
(9,22,1.00,'unidad','barra pequeña o bocadillo'),
(9,25,3.00,'g',NULL),
(10,19,500.00,'g',NULL),
(10,20,7.00,'g','levadura seca'),
(10,24,325.00,'ml','agua templada'),
(10,25,10.00,'g',NULL);

-- ===========================================
-- UTENSILS
-- ===========================================
INSERT INTO Utensils (name)
VALUES
('olla'),
('sartén'),
('batidora'),
('horno'),
('espátula'),
('colador'),
('rallador'),
('cazo'),
('tabla de cortar'),
('cuchillo');

-- ===========================================
-- RECIPE UTENSILS
-- ===========================================
INSERT INTO RecipeUtensils (recipe_id, utensil_id)
VALUES
(1,1),(1,3),(1,6),
(2,3),(2,4),
(3,2),(3,1),
(4,2),(4,8),
(5,4),(5,5),
(6,1),(6,2),(6,5),
(7,2),(7,9),
(8,9),(8,10),
(9,2),(9,10),
(10,4),(10,8);

-- ===========================================
-- MEDIA
-- ===========================================
INSERT INTO Media (owner_user_id, recipe_id, url, type, order_index)
VALUES
(1,1,'https://picsum.photos/200?food1','image',1),
(2,2,'https://picsum.photos/200?food2','image',1),
(3,3,'https://picsum.photos/200?food3','image',1),
(4,4,'https://picsum.photos/200?food4','image',1),
(5,5,'https://picsum.photos/200?food5','image',1),
(6,6,'https://picsum.photos/200?food6','image',1),
(7,7,'https://picsum.photos/200?food7','image',1),
(8,8,'https://picsum.photos/200?food8','image',1),
(9,9,'https://picsum.photos/200?food9','image',1),
(10,10,'https://picsum.photos/200?food10','image',1);

-- ===========================================
-- SOCIAL
-- ===========================================
UPDATE Users SET role = 'ADMIN' WHERE username = 'camaron';

INSERT INTO Follows (follower_id, followed_id)
VALUES
(1,2),(3,1),(3,2),(4,1),(5,2),(6,3),(7,1),(8,4),(9,5),(10,6);

INSERT INTO Likes (user_id, recipe_id)
VALUES
(1,2),(2,1),(3,1),(3,2),(4,5),(5,1),(6,3),(7,4),(8,2),(9,5),
(10,6),(1,7),(2,8),(3,9),(4,10);

INSERT INTO Comments (user_id, recipe_id, text)
VALUES
(3,1,'¡Probé esta pasta y está increíble!'),
(1,2,'El tiramisú está buenísimo.'),
(2,3,'Buena idea para algo rápido.'),
(4,4,'Tofu con curry = felicidad.'),
(5,5,'Brownie de locura, literal.'),
(6,6,'Las croquetas han quedado brutales.'),
(7,7,'Salsa brava de verdad, sí señor.'),
(8,8,'Muy fresca para el verano.'),
(9,9,'Bocadillo top para salir al monte.'),
(10,10,'Pan fácil y queda genial.');

-- ===========================================
-- ACHIEVEMENTS
-- ===========================================
INSERT INTO Achievements (name, description, icon_url, category, points)
VALUES
('Tu primera receta','Publica tu primera receta en PlateUp.','https://picsum.photos/50?achievement_recipe_1','recipes',50),
('Chef constante','Publica 10 recetas.','https://picsum.photos/50?achievement_recipe_2','recipes',100),
('Maestro de la cocina','Publica 25 recetas.','https://picsum.photos/50?achievement_recipe_3','recipes',200),
('Primer aplauso','Recibe tu primer like en una de tus recetas.','https://picsum.photos/50?achievement_like_1','likes',40),
('Receta popular','Recibe 10 likes en total en tus recetas.','https://picsum.photos/50?achievement_like_2','likes',100),
('Fenómeno foodie','Recibe 25 likes en total en tus recetas.','https://picsum.photos/50?achievement_like_3','likes',180),
('Primera receta cocinada','Termina de cocinar tu primera receta.','https://picsum.photos/50?achievement_cooked_1','cooked',50),
('Cocinillas','Termina de cocinar 10 recetas.','https://picsum.photos/50?achievement_cooked_2','cooked',110),
('Chef imparable','Termina de cocinar 25 recetas.','https://picsum.photos/50?achievement_cooked_3','cooked',220),
('Tu primer seguidor','Consigue tu primer seguidor.','https://picsum.photos/50?achievement_followers_1','followers',60),
('Creador con comunidad','Consigue 10 seguidores.','https://picsum.photos/50?achievement_followers_2','followers',130),
('Estrella de PlateUp','Consigue 25 seguidores.','https://picsum.photos/50?achievement_followers_3','followers',250);

-- ===========================================
-- CHALLENGES
-- ===========================================
INSERT INTO Challenges (name, description, start_date, end_date, goal, reward_points)
VALUES
('Reto vegetariano','Publica 3 recetas sin carne','2025-10-01','2025-10-31','3 recetas',150),
('Semana del postre','Prepara un postre cada día','2025-11-01','2025-11-07','7 recetas',200),
('Sin fritos','Crea 5 recetas sin freír','2025-10-01','2025-10-31','5 recetas',100);

INSERT INTO UserChallenges (user_id, challenge_id, progress, status)
VALUES
(1,1,0.60,'active'),
(2,2,1.00,'completed'),
(3,3,0.30,'active');