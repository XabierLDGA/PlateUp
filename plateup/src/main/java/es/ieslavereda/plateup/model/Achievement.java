package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Representa un logro que puede desbloquear un usuario dentro de la app
@Entity
@Table(name = "Achievements")
public class Achievement {

    // Identificador único del logro en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre, descripción e icono que se muestran al usuario cuando consigue el logro
    private String name;
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    // Categoría a la que pertenece el logro y puntos que otorga al desbloquearse
    private String category;
    private int points;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
