package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RecipeIngredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    // Decimal para soportar cantidades como 1.5 o 0.25
    @Column(name = "quantity_decimal")
    private Double quantityDecimal;

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