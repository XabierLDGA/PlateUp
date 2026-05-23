package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar que representa la clave primaria compuesta de UserChallenge
// JPA la necesita para identificar de forma única la participación de cada usuario en un reto
public class UserChallengeId implements Serializable {
    private Long user_id;
    private Long challenge_id;

    public UserChallengeId() {}
    public UserChallengeId(Long user_id, Long challenge_id) {
        this.user_id = user_id;
        this.challenge_id = challenge_id;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChallengeId that)) return false;
        return Objects.equals(user_id, that.user_id) && Objects.equals(challenge_id, that.challenge_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, challenge_id);
    }
}
