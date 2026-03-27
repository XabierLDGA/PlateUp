package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Collection;
import es.ieslavereda.plateup.repository.CollectionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collections")
@CrossOrigin(origins = "*")
public class CollectionController {

    private final CollectionRepository repository;

    public CollectionController(CollectionRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todas las colecciones
    @GetMapping
    public List<Collection> getAll() {
        return repository.findAll();
    }

    // 🔹 GET colección por ID
    @GetMapping("/{id}")
    public Optional<Collection> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 GET colecciones por usuario
    @GetMapping("/user/{userId}")
    public List<Collection> getByUserId(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    // 🔹 POST crear nueva colección
    @PostMapping
    public Collection create(@RequestBody Collection collection) {
        return repository.save(collection);
    }

    // 🔹 PUT actualizar colección
    @PutMapping("/{id}")
    public Collection update(@PathVariable Long id, @RequestBody Collection collection) {
        collection.setId(id);
        return repository.save(collection);
    }

    // 🔹 DELETE eliminar colección
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
