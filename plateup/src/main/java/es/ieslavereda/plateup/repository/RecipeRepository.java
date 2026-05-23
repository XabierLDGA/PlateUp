package es.ieslavereda.plateup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.ieslavereda.plateup.model.Recipe;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByCreatedAtDescIdDesc();

    List<Recipe> findByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    long countByUserId(Long userId);

    Optional<Recipe> findTopByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    List<Recipe> findByIdIn(Collection<Long> ids);

    // Devuelve el ID del usuario que publicó una receta, sin cargar el objeto completo
    @Query("SELECT r.userId FROM Recipe r WHERE r.id = :recipeId")
    Optional<Long> findUserIdById(@Param("recipeId") Long recipeId);

    // Construye el feed personalizado del usuario: sus propias recetas más las de los usuarios que sigue
    @Query("SELECT r FROM Recipe r WHERE r.userId = :userId " +
           "OR r.userId IN (SELECT f.followedId FROM Follow f WHERE f.followerId = :userId AND f.status = 'accepted') " +
           "ORDER BY r.createdAt DESC, r.id DESC")
    Page<Recipe> findFeedForUser(@Param("userId") Long userId, Pageable pageable);

    // Igual que el feed general pero filtrado por categoría de receta
    @Query("SELECT r FROM Recipe r WHERE (r.userId = :userId " +
           "OR r.userId IN (SELECT f.followedId FROM Follow f WHERE f.followerId = :userId AND f.status = 'accepted')) " +
           "AND r.category = :category " +
           "ORDER BY r.createdAt DESC, r.id DESC")
    Page<Recipe> findFeedForUserAndCategory(@Param("userId") Long userId, @Param("category") String category, Pageable pageable);

    // Agrupa el total de recetas por categoría para mostrar estadísticas en el panel de administración
    @Query("SELECT r.category, COUNT(r) FROM Recipe r GROUP BY r.category ORDER BY COUNT(r) DESC")
    List<Object[]> countByCategory();

    // Devuelve los usuarios que más recetas han publicado, útil para el ranking del panel de admin
    @Query("SELECT r.userId, COUNT(r) FROM Recipe r GROUP BY r.userId ORDER BY COUNT(r) DESC")
    List<Object[]> countByUserGrouped(Pageable pageable);
}