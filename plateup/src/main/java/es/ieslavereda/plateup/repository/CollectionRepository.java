package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Collection;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}