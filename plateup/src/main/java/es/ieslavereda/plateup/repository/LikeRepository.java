package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;

import java.util.Collection;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    void deleteByUserId(Long userId);
    void deleteByRecipeIdIn(Collection<Long> recipeIds);
}