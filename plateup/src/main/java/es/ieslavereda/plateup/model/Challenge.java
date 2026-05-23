package es.ieslavereda.plateup.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// Representa un reto que la app propone a los usuarios durante un período de tiempo concreto
@Entity
@Table(name = "Challenges")
public class Challenge {

    // Identificador único del reto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre y descripción que se muestran al usuario para explicarle en qué consiste el reto
    private String name;
    private String description;

    // Fechas de inicio y fin que delimitan el período en el que el reto está activo
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // Objetivo que debe alcanzar el usuario para completar el reto
    private String goal;

    // Puntos que se otorgan al usuario cuando completa el reto con éxito
    @Column(name = "reward_points")
    private int rewardPoints;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public int getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }
}
