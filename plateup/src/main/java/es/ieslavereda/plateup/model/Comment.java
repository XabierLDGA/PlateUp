package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

// Representa un comentario que un usuario ha escrito sobre una receta concreta
@Entity
@Table(name = "Comments")
public class Comment {

    // Identificador único del comentario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que ha escrito el comentario y receta sobre la que opina
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "recipe_id")
    private Long recipeId;

    // Texto del comentario; no puede estar vacío
    @NotBlank(message = "Comment text is required")
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
