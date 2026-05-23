package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;

import java.util.Collection;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findByRecipeId(Long recipeId);
    List<Like> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByRecipeIdIn(Collection<Long> recipeIds);

    // Cuenta el total de likes que han recibido todas las recetas publicadas por un usuario
    @Query(value = "SELECT COUNT(*) FROM Likes l INNER JOIN Recipes r ON r.id = l.recipe_id WHERE r.user_id = :userId", nativeQuery = true)
    long countLikesReceivedByUserId(@Param("userId") Long userId);

    // Devuelve el número de likes de cada receta para un conjunto de IDs dado, útil para cargar el feed en bloque
    @Query("SELECT l.recipeId, COUNT(l) FROM Like l WHERE l.recipeId IN :recipeIds GROUP BY l.recipeId")
    List<Object[]> countGroupByRecipeId(@Param("recipeIds") Collection<Long> recipeIds);

    // Devuelve las recetas más populares ordenadas por número de likes, con paginación
    @Query("SELECT l.recipeId, COUNT(l) FROM Like l GROUP BY l.recipeId ORDER BY COUNT(l) DESC")
    List<Object[]> topRecipesByLikes(org.springframework.data.domain.Pageable pageable);
}