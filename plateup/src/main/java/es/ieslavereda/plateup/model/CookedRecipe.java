package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Registra cada vez que un usuario cocina una receta, guardando cuándo lo hizo y cuánto tardó
@Entity
@Table(name = "CookedRecipes")
public class CookedRecipe {

    // Identificador único de este registro de cocinado
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que cocinó la receta y qué receta cocinó
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    // Momento exacto en el que el usuario terminó de cocinar
    @Column(name = "cooked_at")
    private LocalDateTime cookedAt;

    // Tiempo en segundos que tardó el usuario en cocinar la receta según el temporizador de la app
    @Column(name = "elapsed_seconds")
    private Integer elapsedSeconds;

    // ── Getters & Setters ──────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public LocalDateTime getCookedAt() { return cookedAt; }
    public void setCookedAt(LocalDateTime cookedAt) { this.cookedAt = cookedAt; }

    public Integer getElapsedSeconds() { return elapsedSeconds; }
    public void setElapsedSeconds(Integer elapsedSeconds) { this.elapsedSeconds = elapsedSeconds; }
}