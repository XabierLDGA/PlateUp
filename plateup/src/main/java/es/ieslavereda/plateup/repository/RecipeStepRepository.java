package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.RecipeStep;

import java.util.Collection;
import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    List<RecipeStep> findByRecipeId(Long recipeId);

    void deleteByRecipeIdIn(Collection<Long> recipeIds);
}