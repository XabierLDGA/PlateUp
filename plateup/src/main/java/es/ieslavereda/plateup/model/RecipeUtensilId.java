package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar que representa la clave primaria compuesta de RecipeUtensil
// JPA la necesita para identificar de forma única la relación entre una receta y un utensilio
public class RecipeUtensilId implements Serializable {
    private Long recipe_id;
    private Long utensil_id;

    public RecipeUtensilId() {}
    public RecipeUtensilId(Long recipe_id, Long utensil_id) {
        this.recipe_id = recipe_id;
        this.utensil_id = utensil_id;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeUtensilId that = (RecipeUtensilId) o;
        return Objects.equals(recipe_id, that.recipe_id) && Objects.equals(utensil_id, that.utensil_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe_id, utensil_id);
    }
}
