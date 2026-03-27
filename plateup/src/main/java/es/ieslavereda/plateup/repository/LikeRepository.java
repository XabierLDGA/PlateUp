package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {}
