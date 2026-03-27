package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "RecipeUtensils")
@IdClass(RecipeUtensilId.class)
public class RecipeUtensil {

    @Id
    private Long recipe_id;

    @Id
    private Long utensil_id;

    // Getters y Setters
    public Long getRecipe_id() { return recipe_id; }
    public void setRecipe_id(Long recipe_id) { this.recipe_id = recipe_id; }
    public Long getUtensil_id() { return utensil_id; }
    public void setUtensil_id(Long utensil_id) { this.utensil_id = utensil_id; }
}
