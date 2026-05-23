package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Representa una colección de recetas que un usuario crea para organizar y guardar sus favoritas
@Entity
@Table(name = "Collections")
public class Collection {

    // Identificador único de la colección
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario propietario de la colección y nombre que le ha dado
    @Column(name = "user_id")
    private Long userId;

    private String name;

    // Indica si la colección es visible para otros usuarios o solo para su dueño
    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
