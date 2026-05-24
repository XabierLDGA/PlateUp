SET FOREIGN_KEY_CHECKS = 0;
-- ===========================================
-- Base de datos: PlateUp
-- Script corregido y consistente con backend/frontend
-- ===========================================

-- Forzamos UTF-8 para la sesión de conexión, necesario en algunos clientes MySQL
SET NAMES utf8mb4;

-- Recreamos la base de datos desde cero con soporte completo para emojis y caracteres especiales
USE railway;

-- ===========================================
-- USERS
-- ===========================================
-- Tabla de usuarios: incluye perfil, credenciales, racha de actividad y rol (USER/ADMIN)
DROP TABLE IF EXISTS Users;
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
DROP TABLE IF EXISTS Recipes;
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
DROP TABLE IF EXISTS RecipeSteps;
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
DROP TABLE IF EXISTS Ingredients;
CREATE TABLE Ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    unit_default VARCHAR(20)
);

-- ===========================================
-- RECIPE INGREDIENTS
-- ===========================================
DROP TABLE IF EXISTS RecipeIngredients;
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
DROP TABLE IF EXISTS Utensils;
CREATE TABLE Utensils (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- ===========================================
-- RECIPE UTENSILS
-- ===========================================
DROP TABLE IF EXISTS RecipeUtensils;
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
DROP TABLE IF EXISTS Media;
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
DROP TABLE IF EXISTS Follows;
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
DROP TABLE IF EXISTS Likes;
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
DROP TABLE IF EXISTS Comments;
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
DROP TABLE IF EXISTS Collections;
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
DROP TABLE IF EXISTS CollectionRecipes;
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
DROP TABLE IF EXISTS Achievements;
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
DROP TABLE IF EXISTS UserAchievements;
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
DROP TABLE IF EXISTS Challenges;
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
DROP TABLE IF EXISTS UserChallenges;
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
DROP TABLE IF EXISTS Notifications;
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
DROP TABLE IF EXISTS Reports;
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
DROP TABLE IF EXISTS AuditLogs;
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
-- Registra qué recetas ha cocinado cada usuario y cuánto tiempo tardó
DROP TABLE IF EXISTS CookedRecipes;
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
-- Índices para agilizar las consultas más frecuentes de la aplicación
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
-- Vaciamos todas las tablas antes de insertar los datos de prueba para evitar duplicados
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
(1, 'Pasta al pesto', 'Receta clásica italiana con albahaca y piñones.', 'Pasta', 'https://rana-cdn.thron.com/delivery/public/image/rana/c8561593-a8dd-4404-96ea-451d7af2f4c3/gg3i3t/std/0x0/ES-ricetta0-T_3T1A4388.jpg', 2, 25, 'easy', '2026-03-17 13:24:00'),
(2, 'Tiramisú casero', 'Postre italiano con mascarpone y café.', 'Dessert', 'https://recetasdecocina.elmundo.es/wp-content/uploads/2022/08/tiramisu-postre-italiano.jpg', 6, 40, 'medium', '2026-03-18 20:11:00'),
(3, 'Arroz con verduras', 'Rápido, barato y perfecto para estudiantes.', 'Rice', 'https://www.recetasdesbieta.com/wp-content/uploads/2016/05/arroz-con-verduras-1.jpg', 2, 20, 'easy', '2026-03-19 09:42:00'),
(4, 'Tofu al curry', 'Sabor potente y cero carne.', 'Vegan', 'https://www.lagloriavegana.com/wp-content/uploads/2020/09/IMG_9281-1280x1280.jpg', 3, 30, 'medium', '2026-03-20 14:37:00'),
(5, 'Brownie loco', 'Mucho chocolate y poca vergüenza.', 'Dessert', 'https://images.aws.nestle.recipes/original/2024_10_23T08_38_28_badun_images.badun.es_ac5fa47c04dd_brownie_de_chocolate_negro_47b9.jpg', 8, 35, 'medium', '2026-03-21 18:56:00'),
(6, 'Croquetas de pollo', 'Receta de la abuela, infalible.', 'Meat', 'https://www.pequerecetas.com/wp-content/uploads/2015/09/croquetas-de-pollo-receta.jpg', 6, 70, 'hard', '2026-03-22 12:08:00'),
(7, 'Patatas bravas crujientes', 'Con salsa de verdad, no ketchup.', 'Snack', 'https://www.goya.com/wp-content/uploads/2023/10/spicy-potatoes.jpg', 4, 25, 'easy', '2026-03-22 19:21:00'),
(8, 'Ensalada detox', 'Verde, ligera y con chispa.', 'Salad', 'https://exoticfruitbox.com/wp-content/uploads/2017/01/ensalada-detox-con-frutas-tropicales-dos.jpg', 2, 10, 'easy', '2026-03-23 08:17:00'),
(9, 'Bocadillo de tortilla', 'Ideal para excursiones.', 'Student', 'https://thumbs.dreamstime.com/b/espa%C3%A3%C2%B1ola-de-bocadillo-tortilla-espa%C3%B1ol-la-patata-en-tabla-madera-139469689.jpg', 1, 15, 'easy', '2026-03-23 10:03:00'),
(10, 'Pan casero rápido', 'Hecho sin amasado, fácil y rico.', 'Quick', 'https://images.unsplash.com/photo-1608198093002-ad4e005484ec?auto=format&fit=crop&w=1200&q=80', 8, 180, 'medium', '2026-03-23 11:26:00'),
(1,  'Gazpacho andaluz',       'El clásico andaluz frío, perfecto para el verano.',  'Soup',       'https://i.blogs.es/e64620/gazpacho/1200_900.jpg', 4, 15,  'easy',   '2026-03-24 11:00:00'),
(2,  'Crepes de desayuno',     'Finas y esponjosas, ideales para empezar el día.',   'Breakfast',  'https://recetasdecocina.elmundo.es/wp-content/uploads/2024/10/crepes-receta-1024x683.jpg', 4, 20,  'easy',   '2026-03-24 13:00:00'),
(6,  'Pulpo a la gallega',     'Auténtica receta gallega con pimentón y aceite.',    'Fish',       'https://imag.bonviveur.com/pulpo-a-la-gallega-recien-hecho.jpg', 4, 90,  'hard',   '2026-03-25 10:00:00'),
(11, 'Salmón con aguacate',    'Rápido, sano y con mucho estilo.',                   'Healthy',    'https://newluxbrand.com/recetas/wp-content/uploads/2023/05/Mayo23_V100_tostadasdeaguacatesamonahumadoyhuevo_01.jpg', 2, 20,  'easy',   '2026-03-25 14:00:00'),
(3,  'Pisto manchego',         'Verduras de temporada a fuego lento.',               'Vegetarian', 'https://recetasdecocina.elmundo.es/wp-content/uploads/2025/03/pisto-receta.jpg', 4, 40,  'medium', '2026-03-26 12:00:00'),
(1, 'Bowl proteico de pollo', 'Completo, equilibrado y listo en 30 minutos.',       'Healthy',    'https://www.arrozsos.es/wp-content/uploads/2025/05/1080x720-4-960x640.jpg',  2, 30,  'easy',   '2026-03-26 14:00:00'),
(4,  'Sopa de cebolla',        'Gratinada, calentita y muy reconfortante.',          'Soup',       'https://recetasdecocina.elmundo.es/wp-content/uploads/2024/10/sopa-de-cebolla-receta.jpg', 4, 45,  'medium', '2026-03-27 19:00:00'),
(7,  'Cocido madrileño',       'El plato de cuchara más contundente de Madrid.',     'Meat',       'https://www.laespanolaaceites.com/wp-content/uploads/2019/06/cocido-madrileno-1080x671.jpg', 6, 180, 'hard',   '2026-03-27 20:00:00'),
(8,  'Tortitas de avena',      'Desayuno energético para deportistas.',              'Breakfast',  'https://i.blogs.es/d26a8a/tortitas-de-avena/1200_900.jpg', 2, 15,  'easy',   '2026-03-28 08:00:00'),
(5,  'Tarta de chocolate',     'Intensa, húmeda y sin remordimientos.',              'Dessert',    'https://assets.tmecosys.com/image/upload/t_web_rdp_recipe_584x480_1_5x/img/recipe/ras/Assets/E3C6C9D0-801E-4F1C-9CDC-7DC69801CDB0/Derivates/B3B5036E-103F-4644-B19D-89D0501D93B5.jpg', 8, 60,  'hard',   '2026-03-28 18:00:00');

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
-- Asignamos el rol de administrador al usuario principal de prueba
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
-- Logros del sistema de gamificación agrupados por categoría: recetas, likes, cocinadas y seguidores
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

-- ===========================================
-- PASOS DE LAS RECETAS NUEVAS
-- ===========================================
INSERT INTO RecipeSteps (recipe_id, order_index, text, timer_seconds)
VALUES
-- Gazpacho (11)
(11,1,'Trocea tomate, pepino, pimiento y ajo en trozos grandes.',NULL),
(11,2,'Tritura todo con la batidora junto al aceite, vinagre y sal hasta que quede fino.',NULL),
(11,3,'Cuela, ajusta de sal y enfría en la nevera al menos 1 hora.',3600),
-- Crepes (12)
(12,1,'Mezcla la harina, los huevos, la leche y una pizca de sal hasta obtener una masa lisa.',NULL),
(12,2,'Deja reposar la masa 10 minutos. Cocina porciones finas en sartén a fuego medio.',600),
(12,3,'Sirve con miel, mermelada o fruta al gusto.',NULL),
-- Pulpo a la gallega (13)
(13,1,'Cuece el pulpo en agua hirviendo durante 40-45 minutos, dejándolo enfriar en el propio caldo.',2700),
(13,2,'Cuece las patatas aparte con sal hasta que estén tiernas.',NULL),
(13,3,'Corta el pulpo en rodajas, colócalo sobre las patatas y aliña con aceite, pimentón y sal gruesa.',NULL),
-- Salmón con aguacate (14)
(14,1,'Sazona el salmón con sal, pimienta y zumo de limón.',NULL),
(14,2,'Cocínalo en la plancha 3-4 minutos por cada lado.',NULL),
(14,3,'Sirve con el aguacate en láminas y un chorrito de aceite de oliva.',NULL),
-- Pisto manchego (15)
(15,1,'Sofríe la cebolla y el ajo en aceite a fuego medio hasta que estén transparentes.',NULL),
(15,2,'Añade el pimiento, el calabacín y el tomate troceados. Sazona con sal.',NULL),
(15,3,'Cuece a fuego lento 25 minutos, removiendo de vez en cuando.',1500),
-- Bowl proteico (16)
(16,1,'Cuece el arroz según las instrucciones del paquete.',NULL),
(16,2,'Asa el pollo a la plancha con sal, aceite y zumo de limón hasta que esté hecho.',NULL),
(16,3,'Monta el bowl con el arroz de base, el pollo en tiras y el aguacate en láminas.',NULL),
-- Sopa de cebolla (17)
(17,1,'Sofríe la cebolla en mantequilla a fuego lento hasta caramelizar, unos 20 minutos.',1200),
(17,2,'Añade el caldo y cuece 15 minutos más. Ajusta de sal.',900),
(17,3,'Sirve en cuencos con rebanadas de pan tostado y queso parmesano gratinado.',NULL),
-- Cocido madrileño (18)
(18,1,'Remoja los garbanzos en agua fría la noche anterior.',NULL),
(18,2,'Cuece los garbanzos con el pollo, chorizo, morcilla y zanahoria a fuego lento durante 2 horas.',7200),
(18,3,'Sirve en tres tiempos: primero el caldo, luego las verduras y finalmente las carnes.',NULL),
-- Tortitas de avena (19)
(19,1,'Mezcla la avena con el huevo, la leche y la miel hasta obtener una masa homogénea.',NULL),
(19,2,'Cocina porciones pequeñas en sartén antiadherente a fuego medio, 2 minutos por lado.',NULL),
(19,3,'Sirve con fruta fresca y un poco más de miel por encima.',NULL),
-- Tarta de chocolate (20)
(20,1,'Funde el chocolate negro con la mantequilla al baño María.',NULL),
(20,2,'Fuera del fuego, mezcla con el azúcar, los huevos y la harina hasta integrar bien.',NULL),
(20,3,'Vierte en un molde engrasado y hornea a 180°C durante 35 minutos.',2100);

-- ===========================================
-- INGREDIENTES NUEVOS (IDs 26-45)
-- ===========================================
INSERT INTO Ingredients (name, unit_default)
VALUES
('tomate','g'),
('pepino','g'),
('pimiento','g'),
('ajo','diente'),
('vinagre','ml'),
('pulpo','g'),
('pimentón','g'),
('salmón','g'),
('aguacate','unidad'),
('cebolla','g'),
('caldo de verduras','ml'),
('avena','g'),
('leche','ml'),
('miel','g'),
('chocolate negro','g'),
('nata','ml'),
('garbanzos','g'),
('chorizo','g'),
('morcilla','g'),
('zanahoria','g');

-- ===========================================
-- INGREDIENTES DE LAS RECETAS NUEVAS
-- ===========================================
INSERT INTO RecipeIngredients (recipe_id, ingredient_id, quantity_decimal, unit, notes)
VALUES
-- Gazpacho (11): tomate=26, pepino=27, pimiento=28, ajo=29, aceite=5, vinagre=30, sal=25, agua=24
(11,26,800.00,'g','bien maduros'),
(11,27,0.50,'unidad',NULL),
(11,28,0.50,'unidad','sin semillas'),
(11,29,1.00,'diente',NULL),
(11,5,50.00,'ml','aceite extra virgen'),
(11,30,20.00,'ml','vinagre de jerez'),
(11,25,5.00,'g',NULL),
(11,24,100.00,'ml','agua fría'),
-- Crepes (12): harina=19, huevo=21, leche=38, mantequilla=23, azúcar=9, sal=25
(12,19,150.00,'g',NULL),
(12,21,2.00,'unidad',NULL),
(12,38,300.00,'ml',NULL),
(12,23,30.00,'g',NULL),
(12,9,15.00,'g','opcional'),
(12,25,2.00,'g',NULL),
-- Pulpo a la gallega (13): pulpo=31, patata=15, pimentón=32, aceite=5, sal=25
(13,31,1000.00,'g','ya limpio'),
(13,15,400.00,'g',NULL),
(13,32,5.00,'g','pimentón dulce o picante al gusto'),
(13,5,40.00,'ml',NULL),
(13,25,8.00,'g','sal gruesa'),
-- Salmón con aguacate (14): salmón=33, aguacate=34, limón=18, aceite=5, sal=25
(14,33,300.00,'g','en filetes'),
(14,34,1.00,'unidad',NULL),
(14,18,0.50,'unidad','zumo'),
(14,5,15.00,'ml',NULL),
(14,25,3.00,'g',NULL),
-- Pisto manchego (15): pimiento=28, tomate=26, cebolla=35, ajo=29, aceite=5, sal=25
(15,28,2.00,'unidad',NULL),
(15,26,3.00,'unidad','grandes y maduros'),
(15,35,1.00,'unidad','grande'),
(15,29,2.00,'diente',NULL),
(15,5,30.00,'ml',NULL),
(15,25,5.00,'g',NULL),
-- Bowl proteico (16): pollo=14, arroz=6, aguacate=34, limón=18, aceite=5, sal=25
(16,14,200.00,'g','pechuga'),
(16,6,150.00,'g',NULL),
(16,34,1.00,'unidad',NULL),
(16,18,0.50,'unidad','zumo'),
(16,5,20.00,'ml',NULL),
(16,25,4.00,'g',NULL),
-- Sopa de cebolla (17): cebolla=35, caldo=36, mantequilla=23, pan=22, queso=4, sal=25
(17,35,600.00,'g','unas 4 cebollas grandes'),
(17,36,1000.00,'ml',NULL),
(17,23,40.00,'g',NULL),
(17,22,4.00,'unidad','rebanadas gruesas'),
(17,4,60.00,'g',NULL),
(17,25,4.00,'g',NULL),
-- Cocido madrileño (18): garbanzos=42, pollo=14, chorizo=43, morcilla=44, patata=15, zanahoria=45, sal=25, agua=24
(18,42,300.00,'g','remojados la noche anterior'),
(18,14,300.00,'g',NULL),
(18,43,150.00,'g',NULL),
(18,44,100.00,'g',NULL),
(18,15,300.00,'g',NULL),
(18,45,2.00,'unidad',NULL),
(18,25,10.00,'g',NULL),
(18,24,2000.00,'ml',NULL),
-- Tortitas de avena (19): avena=37, huevo=21, leche=38, miel=39, mantequilla=23
(19,37,100.00,'g',NULL),
(19,21,1.00,'unidad',NULL),
(19,38,80.00,'ml',NULL),
(19,39,10.00,'g',NULL),
(19,23,10.00,'g','para engrasar la sartén'),
-- Tarta de chocolate (20): chocolate=40, mantequilla=23, azúcar=9, huevo=21, harina=19, nata=41
(20,40,200.00,'g','70% cacao'),
(20,23,100.00,'g',NULL),
(20,9,150.00,'g',NULL),
(20,21,3.00,'unidad',NULL),
(20,19,50.00,'g',NULL),
(20,41,50.00,'ml','nata para cocinar');

-- ===========================================
-- UTENSILIOS NUEVOS
-- ===========================================
INSERT INTO Utensils (name)
VALUES
('plancha'),
('molde');

-- ===========================================
-- UTENSILIOS DE LAS RECETAS NUEVAS
-- plancha=11, molde=12; existentes: olla=1, sartén=2, batidora=3, horno=4, espátula=5, colador=6, cazo=8, tabla=9, cuchillo=10
-- ===========================================
INSERT INTO RecipeUtensils (recipe_id, utensil_id)
VALUES
(11,3),(11,6),
(12,2),(12,5),
(13,1),(13,9),(13,10),
(14,11),(14,9),
(15,2),(15,9),(15,10),
(16,1),(16,11),(16,9),
(17,8),(17,9),(17,4),
(18,1),(18,9),(18,10),
(19,2),(19,5),
(20,4),(20,12),(20,8);

-- ===========================================
-- MEDIA DE LAS RECETAS NUEVAS
-- ===========================================
INSERT INTO Media (owner_user_id, recipe_id, url, type, order_index)
VALUES
(1,11,'https://picsum.photos/200?food11','image',1),
(2,12,'https://picsum.photos/200?food12','image',1),
(6,13,'https://picsum.photos/200?food13','image',1),
(11,14,'https://picsum.photos/200?food14','image',1),
(3,15,'https://picsum.photos/200?food15','image',1),
(10,16,'https://picsum.photos/200?food16','image',1),
(4,17,'https://picsum.photos/200?food17','image',1),
(7,18,'https://picsum.photos/200?food18','image',1),
(8,19,'https://picsum.photos/200?food19','image',1),
(5,20,'https://picsum.photos/200?food20','image',1);

-- ===========================================
-- SOCIAL ADICIONAL (follows, likes, comentarios sobre recetas nuevas)
-- ===========================================
INSERT INTO Follows (follower_id, followed_id)
VALUES
(2,3),(4,2),(5,3),(6,1),(7,2),(8,1),(9,2),(10,1),
(11,1),(11,2),(1,11),(2,6),(3,4),(4,6),(5,6),(6,2),(7,3),(8,5),(9,6),(10,7);

INSERT INTO Likes (user_id, recipe_id)
VALUES
(2,11),(3,11),(4,11),(9,11),(11,11),
(5,12),(6,12),(10,12),
(1,13),(7,13),(8,13),
(2,14),(9,14),(10,14),
(3,15),(4,15),
(4,16),(5,16),
(5,17),(6,17),
(6,18),(8,18),
(1,19),(7,19),
(3,20),(8,20),(9,20);

INSERT INTO Comments (user_id, recipe_id, text)
VALUES
(2,11,'El gazpacho perfecto para el verano, lo haré este fin de semana.'),
(3,12,'Crepes esponjosas, mis hijos las han devorado.'),
(1,13,'¡Por fin alguien que sabe hacer pulpo como Dios manda!'),
(5,14,'Salmón con aguacate y tan fácil, increíble.'),
(6,15,'Pisto manchego como el de mi abuela, gracias.'),
(1,16,'Bowl completo y sabroso, perfecto para después del entreno.'),
(2,17,'Sopa de cebolla gratinada en casa, alucinante.'),
(4,18,'Cocido de verdad, con sus tres vuelcos y todo.'),
(1,19,'Tortitas energéticas para antes de la ruta, las pruebo mañana.'),
(3,20,'Tarta de chocolate brutal, la textura es perfecta.');