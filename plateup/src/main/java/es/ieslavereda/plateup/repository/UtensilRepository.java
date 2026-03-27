package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.ieslavereda.plateup.model.Utensil;

public interface UtensilRepository extends JpaRepository<Utensil, Long> {}
