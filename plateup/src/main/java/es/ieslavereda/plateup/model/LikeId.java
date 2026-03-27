package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

public class LikeId implements Serializable {
    private Long userId;
    private Long recipeId;

    public LikeId() {}
    public LikeId(Long userId, Long recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeId)) return false;
        LikeId that = (LikeId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(recipeId, that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recipeId);
    }
}
