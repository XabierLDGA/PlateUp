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

    // Devuelve todos los utensilios disponibles en el catálogo
    @GetMapping
    public List<Utensil> getAll() {
        return repository.findAll();
    }

    // Devuelve un utensilio concreto por su identificador
    @GetMapping("/{id}")
    public Optional<Utensil> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crea un nuevo utensilio en el catálogo
    @PostMapping
    public Utensil create(@RequestBody Utensil utensil) {
        return repository.save(utensil);
    }

    // Actualiza los datos de un utensilio existente
    @PutMapping("/{id}")
    public Utensil update(@PathVariable Long id, @RequestBody Utensil utensil) {
        utensil.setId(id);
        return repository.save(utensil);
    }

    // Elimina un utensilio del catálogo por su identificador
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
