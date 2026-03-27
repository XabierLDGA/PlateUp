package es.ieslavereda.plateup.model;

import java.io.Serializable;
import java.util.Objects;

public class UserChallengeId implements Serializable {
    private Long user_id;
    private Long challenge_id;

    public UserChallengeId() {}
    public UserChallengeId(Long user_id, Long challenge_id) {
        this.user_id = user_id;
        this.challenge_id = challenge_id;
    }

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
