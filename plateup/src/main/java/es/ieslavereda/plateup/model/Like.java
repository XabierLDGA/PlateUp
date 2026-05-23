package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// Representa el "me gusta" que un usuario da a una receta; cada usuario solo puede dar uno por receta
@Entity
@Table(name = "Likes")
@IdClass(LikeId.class)
public class Like implements Serializable {

    // Clave compuesta: un usuario solo puede tener un like por receta, así se evitan duplicados
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters y Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
