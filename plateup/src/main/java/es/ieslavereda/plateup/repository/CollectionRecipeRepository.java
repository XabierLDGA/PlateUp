package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.CollectionRecipe;
import es.ieslavereda.plateup.model.CollectionRecipeId;

public interface CollectionRecipeRepository extends JpaRepository<CollectionRecipe, CollectionRecipeId> {
}
