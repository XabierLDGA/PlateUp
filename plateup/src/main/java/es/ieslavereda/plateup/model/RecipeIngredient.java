package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

// Relaciona un ingrediente con una receta concreta, indicando la cantidad necesaria y cómo se usa
@Entity
@Table(name = "RecipeIngredients")
public class RecipeIngredient {

    // Identificador propio de esta línea de ingrediente dentro de la receta
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Receta e ingrediente que se relacionan en este registro
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    // Cantidad necesaria del ingrediente expresada en decimal (por ejemplo, 1.5 para "1 y media")
    @Column(name = "quantity_decimal")
    private Double quantityDecimal;

    // Unidad de medida usada para este ingrediente en esta receta y notas adicionales del autor
    private String unit;
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Double getQuantityDecimal() {
        return quantityDecimal;
    }

    public void setQuantityDecimal(Double quantityDecimal) {
        this.quantityDecimal = quantityDecimal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}