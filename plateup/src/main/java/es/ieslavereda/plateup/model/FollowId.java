package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

// Clase auxiliar que representa la clave primaria compuesta de Follow
// JPA la necesita para distinguir cada relación de seguimiento de forma única
public class FollowId implements Serializable {
    private Long followerId;
    private Long followedId;

    public FollowId() {}
    public FollowId(Long followerId, Long followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    // equals y hashCode son obligatorios en las claves compuestas para que JPA pueda compararlas correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(followerId, that.followerId) &&
                Objects.equals(followedId, that.followedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId);
    }
}
