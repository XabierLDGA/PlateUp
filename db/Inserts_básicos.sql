-- ===========================================
-- DATOS DE PRUEBA AMPLIADOS - PlateUp
-- Compatible con el schema actual
-- CORREGIDO: todas las recetas tienen pasos e ingredientes
-- ===========================================

USE plateup;

-- ===========================================
-- LIMPIAR TABLAS (opcional pero recomendable)
-- ===========================================

-- Desactivamos temporalmente las restricciones para poder hacer TRUNCATE sin problemas de orden
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
TRUNCATE TABLE Recipes;
TRUNCATE TABLE Users;

-- Volvemos a activar las restricciones antes de insertar los datos
SET FOREIGN_KEY_CHECKS = 1;

-- ===========================================
-- USERS
-- ===========================================
-- Usuarios de prueba con perfiles variados para simular una comunidad activa
INSERT INTO Users (username, email, password_hash, display_name, bio, avatar_url, visibility_default)
VALUES
('camaron','camaron@example.com','camaron','Camarón Mantis','Soy un camarón mantis.','https://media.istockphoto.com/id/2192691445/es/foto/odontodactylus-scyllarus.jpg?s=612x612&w=0&k=20&c=Taq6oFbSVoMMeaBY5EqgCtWWDrSw04pyegDLvCJcKNE=','public'),
('saratgn','sara@example.com','saratgn','Sara Teglas','La mujer de Xabier López de Guereño Armada.','https://media.licdn.com/dms/image/v2/D5603AQGFtHcFicA-sg/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1719503696706?e=2147483647&v=beta&t=QHPtg3yGspCyvgAt7twdv0_ZYkmIpoIn-1W0So6BGFY','followers'),
('miguelbeltran','miguelbeltran@example.com','miguelbeltran','Miguel Beltrán','Se me ha puesto como una cachiporra.','https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Tamias_striatus_CT.jpg/500px-Tamias_striatus_CT.jpg','public'),
('samuelhdez','samuel@example.com','samuelhdez','Samuel Hernández','El piranya.','https://pbs.twimg.com/media/E6buFZ5WUAcXkjm.jpg','public'),
('davidgimenez','david@example.com','davidgimenez','David Giménez','Erika, vuelve conmigo por favor.','https://yt3.googleusercontent.com/UZsSMbWFhV4DAa66jLUOW4emrwYayCkHyM8VNBQSYCXRCR5GEZjC-2i2kX90xUIj1ihttsFakQM=s160-c-k-c0x00ffffff-no-rj','public'),
('pablogallego','pablogallego@example.com','pablogallego','Pablo Gallego','Cocina y buenas vibes (cocina gallega de la buena).','https://st3.depositphotos.com/1967477/31855/v/450/depositphotos_318559454-stock-illustration-vector-illustration-cute-octopus-cartoon.jpg','public'),
('josebenjamin','jose@example.com','josebenjamin','Jose Benjamín','Cenar a las 19:00, hacer un crucigrama y a dormir.','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5pVwRXMnROJ0kU6Ls0jb9XBgKqMobMfQqrQ&s','followers'),
('lucasgabaldon','lucas@example.com','lucasgabaldon','Lucas Gabaldon','Con la bici por ahí.','https://media.gettyimages.com/id/2117342420/es/foto/portrait-of-homosexual-man-riding-a-bicycle-outdoors.jpg?s=612x612&w=gi&k=20&c=AfraV2DK6qtN-QI_sAuYztpvKWQRzxQrGXA47EBBa8M=','public'),
('guillermomartin','guillermo@example.com','guillermomartin','Guillermo Martin','Yo hago el amor.','https://www.disfracessimon.com/cdn/shop/files/7068_a.jpg?v=1751399681','public'),
('dariopaez','dario@example.com','dariopaez','Dario Paez','Powerlifter de éxito exitosamente exitoso.','https://i.pinimg.com/originals/47/a0/34/47a0343d17b41b9298f31a3e7cb64245.jpg','public'),
('pablogonzalez','pablogonzalez@example.com','pablogonzalez','Pablo Gonzalez','Estoy forrao.','https://static9.depositphotos.com/1594308/1124/i/450/depositphotos_11241524-stock-photo-wealthy-boy.jpg','followers');

-- ===========================================
-- RECIPES
-- ===========================================
-- Recetas de ejemplo con distintas dificultades y tiempos de preparación
INSERT INTO Recipes (user_id, title, description, image_url, servings, total_minutes, difficulty)
VALUES
(1, 'Pasta al pesto', 'Receta clásica italiana con albahaca y piñones.', 'https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1200&q=80', 2, 25, 'easy'),
(2, 'Tiramisú casero', 'Postre italiano con mascarpone y café.', 'https://images.unsplash.com/photo-1512058564366-18510be2db19?auto=format&fit=crop&w=1200&q=80', 6, 40, 'medium'),
(3, 'Arroz con verduras', 'Rápido, barato y perfecto para estudiantes.', 'https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=1200&q=80', 2, 20, 'easy'),
(4, 'Tofu al curry', 'Sabor potente y cero carne.', 'https://images.unsplash.com/photo-1482049016688-2d3e1b311543?auto=format&fit=crop&w=1200&q=80', 3, 30, 'medium'),
(5, 'Brownie loco', 'Mucho chocolate y poca vergüenza.', 'https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=80', 8, 35, 'medium'),
(6, 'Croquetas de pollo', 'Receta de la abuela, infalible.', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=80', 6, 70, 'hard'),
(7, 'Patatas bravas crujientes', 'Con salsa de verdad, no ketchup.', 'https://images.unsplash.com/photo-1529042410759-befb1204b468?auto=format&fit=crop&w=1200&q=80', 4, 25, 'easy'),
(8, 'Ensalada detox', 'Verde, ligera y con chispa.', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1200&q=80', 2, 10, 'easy'),
(9, 'Bocadillo de tortilla', 'Ideal para excursiones.', 'https://images.unsplash.com/photo-1539252554453-80ab65ce3586?auto=format&fit=crop&w=1200&q=80', 1, 15, 'easy'),
(10, 'Pan casero rápido', 'Hecho sin amasado, fácil y rico.', 'https://images.unsplash.com/photo-1608198093002-ad4e005484ec?auto=format&fit=crop&w=1200&q=80', 8, 180, 'medium');

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
-- Relaciones sociales de prueba: follows, likes y comentarios entre usuarios
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
-- Logros base del sistema de gamificación de PlateUp
INSERT INTO Achievements (name, description, icon_url, category, points)
VALUES
('Nuevo chef','Publica tu primera receta','https://picsum.photos/50?ach1','publicación',50),
('Popular','Consigue 10 me gusta','https://picsum.photos/50?ach2','social',100),
('Comentarista','Deja 5 comentarios','https://picsum.photos/50?ach3','interacción',70),
('Explorador','Publica recetas en 3 categorías','https://picsum.photos/50?ach4','creatividad',80),
('Fotógrafo foodie','Sube 10 fotos','https://picsum.photos/50?ach5','visual',60);

INSERT INTO UserAchievements (user_id, achievement_id, level)
VALUES
(1,1,1),(2,2,1),(3,3,1),(4,4,1),(5,5,1);

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