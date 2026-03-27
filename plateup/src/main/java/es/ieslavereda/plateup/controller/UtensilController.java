package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Utensil;
import es.ieslavereda.plateup.repository.UtensilRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utensils")
@CrossOrigin(origins = "*")
public class UtensilController {

    private final UtensilRepository repository;

    public UtensilController(UtensilRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los utensilios
    @GetMapping
    public List<Utensil> getAll() {
        return repository.findAll();
    }

    // 🔹 GET por ID
    @GetMapping("/{id}")
    public Optional<Utensil> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 POST crear nuevo utensilio
    @PostMapping
    public Utensil create(@RequestBody Utensil utensil) {
        return repository.save(utensil);
    }

    // 🔹 PUT actualizar utensilio existente
    @PutMapping("/{id}")
    public Utensil update(@PathVariable Long id, @RequestBody Utensil utensil) {
        utensil.setId(id);
        return repository.save(utensil);
    }

    // 🔹 DELETE eliminar utensilio
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
