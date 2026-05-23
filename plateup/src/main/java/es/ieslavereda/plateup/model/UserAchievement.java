package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Registra qué logros ha conseguido cada usuario y en qué momento los desbloqueó
@Entity
@Table(name = "UserAchievements")
@IdClass(UserAchievementId.class)
public class UserAchievement {

    // Clave compuesta: cada usuario solo puede tener una vez cada logro
    @Id
    private Long user_id;

    @Id
    private Long achievement_id;

    // Nivel alcanzado dentro del logro, para aquellos logros que tienen varias etapas de progreso
    private int level;

    // Fecha y hora exactas en que el usuario ganó el logro; se establece automáticamente por la base de datos
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime earned_at;

    // Getters y setters
    public Long getUser_id() { return user_id; }
    public void setUser_id(Long user_id) { this.user_id = user_id; }
    public Long getAchievement_id() { return achievement_id; }
    public void setAchievement_id(Long achievement_id) { this.achievement_id = achievement_id; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public LocalDateTime getEarned_at() { return earned_at; }
    public void setEarned_at(LocalDateTime earned_at) { this.earned_at = earned_at; }
}
