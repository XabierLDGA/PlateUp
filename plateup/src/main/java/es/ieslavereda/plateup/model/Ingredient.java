package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

// Representa un ingrediente del catálogo global que puede usarse en cualquier receta de la app
@Entity
@Table(name = "Ingredients")
public class Ingredient {

    // Identificador único del ingrediente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del ingrediente y la unidad de medida que se usa habitualmente para él
    private String name;

    @Column(name = "unit_default")
    private String unitDefault;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnitDefault() { return unitDefault; }
    public void setUnitDefault(String unitDefault) { this.unitDefault = unitDefault; }
}
