package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.RecipeUtensil;
import es.ieslavereda.plateup.model.RecipeUtensilId;

public interface RecipeUtensilRepository extends JpaRepository<RecipeUtensil, RecipeUtensilId> {}
