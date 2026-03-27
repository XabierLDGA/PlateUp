package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {}
