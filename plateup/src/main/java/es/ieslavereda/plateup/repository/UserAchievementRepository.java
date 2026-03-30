package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.model.UserAchievementId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> {

    @Query("""
        SELECT CASE WHEN COUNT(ua) > 0 THEN true ELSE false END
        FROM UserAchievement ua
        WHERE ua.user_id = :userId
          AND ua.achievement_id = :achievementId
    """)
    boolean existsByUser_idAndAchievement_id(
            @Param("userId") Long userId,
            @Param("achievementId") Long achievementId
    );

    @Query("""
        SELECT ua
        FROM UserAchievement ua
        WHERE ua.user_id = :userId
    """)
    List<UserAchievement> findByUser_id(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM UserAchievement ua
        WHERE ua.user_id = :userId
    """)
    void deleteByUserId(@Param("userId") Long userId);
}