package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Follow;
import es.ieslavereda.plateup.model.FollowId;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findByFollowedIdAndStatus(Long followedId, String status);
    List<Follow> findByFollowerIdAndStatus(Long followerId, String status);
    long countByFollowedIdAndStatus(Long followedId, String status);
    void deleteByFollowerIdOrFollowedId(Long followerId, Long followedId);
}