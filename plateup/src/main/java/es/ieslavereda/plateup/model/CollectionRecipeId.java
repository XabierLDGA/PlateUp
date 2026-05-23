package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar que representa la clave primaria compuesta de CollectionRecipe
// JPA la necesita para identificar de forma única cada fila de la tabla intermedia
public class CollectionRecipeId implements Serializable {
    private Long collection_id;
    private Long recipe_id;

    public CollectionRecipeId() {}

    public CollectionRecipeId(Long collection_id, Long recipe_id) {
        this.collection_id = collection_id;
        this.recipe_id = recipe_id;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionRecipeId that)) return false;
        return Objects.equals(collection_id, that.collection_id) &&
                Objects.equals(recipe_id, that.recipe_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection_id, recipe_id);
    }
}
