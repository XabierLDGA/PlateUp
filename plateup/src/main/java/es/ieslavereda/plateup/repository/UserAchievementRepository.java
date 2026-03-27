package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.model.UserAchievementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> { }
