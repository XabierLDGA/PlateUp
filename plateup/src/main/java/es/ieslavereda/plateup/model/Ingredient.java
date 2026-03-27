package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
