-- ===========================================
-- USUARIOS Y RECETAS
-- ===========================================

-- Ver todos los usuarios registrados
SELECT * FROM Users;

-- Ver todas las recetas con el nombre del autor
SELECT r.id, r.title, r.difficulty, r.servings, u.display_name AS autor
FROM Recipes r
JOIN Users u ON r.user_id = u.id;

-- Ver los pasos de una receta específica (por ejemplo, receta 1)
SELECT rs.order_index, rs.text, rs.timer_seconds
FROM RecipeSteps rs
WHERE rs.recipe_id = 1
ORDER BY rs.order_index;

-- Ver los ingredientes de una receta con sus cantidades y unidades
SELECT r.title, i.name AS ingrediente, ri.quantity_decimal, ri.unit, ri.notes
FROM RecipeIngredients ri
JOIN Recipes r ON ri.recipe_id = r.id
JOIN Ingredients i ON ri.ingredient_id = i.id
WHERE r.id = 1;

-- Ver los utensilios usados en cada receta
SELECT r.title, u.name AS utensilio
FROM RecipeUtensils ru
JOIN Recipes r ON ru.recipe_id = r.id
JOIN Utensils u ON ru.utensil_id = u.id
ORDER BY r.title;

-- ===========================================
-- SOCIAL
-- ===========================================

-- Ver quién sigue a quién
SELECT f.follower_id AS id_seguidor, u1.username AS seguidor,
       f.followed_id AS id_seguido, u2.username AS seguido
FROM Follows f
JOIN Users u1 ON f.follower_id = u1.id
JOIN Users u2 ON f.followed_id = u2.id;

-- Ver los likes con nombre de usuario y receta
SELECT u.username AS usuario, r.title AS receta
FROM Likes l
JOIN Users u ON l.user_id = u.id
JOIN Recipes r ON l.recipe_id = r.id;

-- Ver comentarios con autor y receta
SELECT c.text AS comentario, u.username AS autor, r.title AS receta
FROM Comments c
JOIN Users u ON c.user_id = u.id
JOIN Recipes r ON c.recipe_id = r.id
ORDER BY c.created_at DESC;

-- Ver colecciones con recetas incluidas
SELECT col.name AS coleccion, u.username AS autor, r.title AS receta
FROM CollectionRecipes cr
JOIN Collections col ON cr.collection_id = col.id
JOIN Users u ON col.user_id = u.id
JOIN Recipes r ON cr.recipe_id = r.id
ORDER BY col.name;

-- Comprobación directa de las relaciones entre colecciones y recetas
USE plateup;
SELECT * FROM CollectionRecipes;


-- ===========================================
-- GAMIFICACIÓN
-- ===========================================

-- Ver todos los logros disponibles
SELECT * FROM Achievements;

-- Ver qué logros tiene cada usuario
SELECT u.username, a.name AS logro, ua.level, ua.earned_at
FROM UserAchievements ua
JOIN Users u ON ua.user_id = u.id
JOIN Achievements a ON ua.achievement_id = a.id
ORDER BY u.username;

-- Ver los retos activos y sus participantes
SELECT c.name AS reto, u.username AS participante, uc.progress, uc.status
FROM UserChallenges uc
JOIN Challenges c ON uc.challenge_id = c.id
JOIN Users u ON uc.user_id = u.id;

-- ===========================================
-- COMPLEMENTARIAS
-- ===========================================

-- Ver notificaciones con origen y destino
SELECT n.id, ru.username AS receptor, au.username AS actor, r.title AS receta, n.type, n.created_at
FROM Notifications n
LEFT JOIN Users ru ON n.recipient_user_id = ru.id
LEFT JOIN Users au ON n.actor_user_id = au.id
LEFT JOIN Recipes r ON n.recipe_id = r.id
ORDER BY n.created_at DESC;

-- Ver reportes de usuarios o recetas
SELECT rp.id, u1.username AS reportero, u2.username AS denunciado, r.title AS receta, rp.reason
FROM Reports rp
LEFT JOIN Users u1 ON rp.reporter_user_id = u1.id
LEFT JOIN Users u2 ON rp.target_user_id = u2.id
LEFT JOIN Recipes r ON rp.recipe_id = r.id;

-- Ver el historial de acciones (auditoría)
SELECT a.id, u.username AS actor, a.entity_type, a.entity_id, a.action, a.created_at
FROM AuditLogs a
LEFT JOIN Users u ON a.actor_user_id = u.id
ORDER BY a.created_at DESC;

-- ===========================================
-- CONSULTAS DE COMPROBACIÓN RÁPIDA
-- ===========================================

-- Contar cuántas recetas tiene cada usuario
SELECT u.username, COUNT(r.id) AS total_recetas
FROM Users u
LEFT JOIN Recipes r ON u.id = r.user_id
GROUP BY u.username;

-- Ver las recetas más populares (por número de likes)
SELECT r.title, COUNT(l.user_id) AS total_likes
FROM Recipes r
LEFT JOIN Likes l ON r.id = l.recipe_id
GROUP BY r.id
ORDER BY total_likes DESC;

-- Ver los ingredientes más usados
SELECT i.name AS ingrediente, COUNT(ri.recipe_id) AS veces_usado
FROM Ingredients i
JOIN RecipeIngredients ri ON i.id = ri.ingredient_id
GROUP BY i.name
ORDER BY veces_usado DESC;

-- Ver qué usuarios han obtenido logros y cuántos
SELECT u.username, COUNT(ua.achievement_id) AS total_logros
FROM Users u
LEFT JOIN UserAchievements ua ON u.id = ua.user_id
GROUP BY u.username;
