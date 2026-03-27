package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.RecipeUtensil;
import es.ieslavereda.plateup.model.RecipeUtensilId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface RecipeUtensilRepository extends JpaRepository<RecipeUtensil, RecipeUtensilId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RecipeUtensil ru WHERE ru.recipe_id IN :recipeIds")
    void deleteByRecipeIds(Collection<Long> recipeIds);
}