package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar para la clave compuesta de RecipeIngredient, aunque en la entidad se usa id autogenerado
// Se mantiene por compatibilidad con la configuración de JPA
public class RecipeIngredientId implements Serializable {

    private Long recipeId;
    private Long ingredientId;

    public RecipeIngredientId() {}

    public RecipeIngredientId(Long recipeId, Long ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredientId)) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return Objects.equals(recipeId, that.recipeId) &&
                Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}