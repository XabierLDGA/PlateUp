package es.ieslavereda.plateup.model;

import jakarta.persistence.*;

// Representa un paso individual del proceso de elaboración de una receta
@Entity
@Table(name = "RecipeSteps")
public class RecipeStep {

    // Identificador único del paso
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Receta a la que pertenece este paso y su posición dentro del proceso de elaboración
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "order_index")
    private int orderIndex;

    // Instrucción escrita del paso que el usuario debe seguir
    private String text;

    // Tiempo en segundos del temporizador asociado a este paso, si el autor ha indicado alguno
    @Column(name = "timer_seconds")
    private Integer timerSeconds;

    // Imagen o vídeo opcional que acompaña al paso para ilustrar mejor la técnica
    @Column(name = "media_url")
    private String mediaUrl;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimerSeconds(Integer timerSeconds) {
        this.timerSeconds = timerSeconds;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}