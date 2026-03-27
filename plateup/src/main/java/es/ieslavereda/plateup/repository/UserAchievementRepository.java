package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.model.UserAchievementId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserAchievement ua WHERE ua.user_id = :userId")
    void deleteByUserId(Long userId);
}