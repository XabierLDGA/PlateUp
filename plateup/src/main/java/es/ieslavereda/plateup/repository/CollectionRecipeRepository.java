package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.CollectionRecipe;
import es.ieslavereda.plateup.model.CollectionRecipeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CollectionRecipeRepository extends JpaRepository<CollectionRecipe, CollectionRecipeId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CollectionRecipe cr WHERE cr.collection_id IN :collectionIds")
    void deleteByCollectionIds(Collection<Long> collectionIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM CollectionRecipe cr WHERE cr.recipe_id IN :recipeIds")
    void deleteByRecipeIds(Collection<Long> recipeIds);
}