package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar que representa la clave primaria compuesta de UserAchievement
// JPA la necesita para identificar de forma única el logro de cada usuario
public class UserAchievementId implements Serializable {
    private Long user_id;
    private Long achievement_id;

    public UserAchievementId() {}
    public UserAchievementId(Long user_id, Long achievement_id) {
        this.user_id = user_id;
        this.achievement_id = achievement_id;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAchievementId that)) return false;
        return Objects.equals(user_id, that.user_id) && Objects.equals(achievement_id, that.achievement_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, achievement_id);
    }
}
