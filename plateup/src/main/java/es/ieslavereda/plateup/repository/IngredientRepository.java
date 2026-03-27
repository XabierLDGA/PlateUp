package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {}
