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

    @Query(value = "SELECT COUNT(*) FROM Likes l INNER JOIN Recipes r ON r.id = l.recipe_id WHERE r.user_id = :userId", nativeQuery = true)
    long countLikesReceivedByUserId(@Param("userId") Long userId);
}