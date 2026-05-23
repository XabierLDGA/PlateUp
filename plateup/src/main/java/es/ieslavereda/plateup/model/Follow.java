package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

// Representa la relación de seguimiento entre dos usuarios de la app
@Entity
@Table(name = "Follows")
@IdClass(FollowId.class)
public class Follow implements Serializable {

    // Clave compuesta: quién sigue (follower) y a quién se sigue (followed)
    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "followed_id")
    private Long followedId;

    // Estado del seguimiento: puede estar pendiente de aceptación o ya activo
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getFollowerId() { return followerId; }
    public void setFollowerId(Long followerId) { this.followerId = followerId; }

    public Long getFollowedId() { return followedId; }
    public void setFollowedId(Long followedId) { this.followedId = followedId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}