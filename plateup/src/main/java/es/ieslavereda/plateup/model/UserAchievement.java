package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userachievements")
@IdClass(UserAchievementId.class)
public class UserAchievement {

    @Id
    private Long user_id;

    @Id
    private Long achievement_id;

    private int level;

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
