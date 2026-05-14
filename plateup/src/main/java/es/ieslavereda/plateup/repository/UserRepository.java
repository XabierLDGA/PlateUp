package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as total " +
                   "FROM Users WHERE created_at >= :since " +
                   "GROUP BY month ORDER BY month ASC", nativeQuery = true)
    List<Object[]> countByMonth(@Param("since") LocalDateTime since);
}