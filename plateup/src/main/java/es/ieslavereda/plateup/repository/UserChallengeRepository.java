package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.UserChallenge;
import es.ieslavereda.plateup.model.UserChallengeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, UserChallengeId> {

    // Elimina todos los retos de un usuario, necesario al borrar su cuenta para no dejar huérfanos
    @Modifying
    @Transactional
    @Query("DELETE FROM UserChallenge uc WHERE uc.user_id = :userId")
    void deleteByUserId(Long userId);
}