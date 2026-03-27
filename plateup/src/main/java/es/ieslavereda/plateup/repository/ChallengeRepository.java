package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {}
