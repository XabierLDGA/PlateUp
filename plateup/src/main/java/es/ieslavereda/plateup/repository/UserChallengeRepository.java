package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.UserChallenge;
import es.ieslavereda.plateup.model.UserChallengeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, UserChallengeId> { }
