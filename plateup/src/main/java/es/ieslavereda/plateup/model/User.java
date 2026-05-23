package es.ieslavereda.plateup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Representa un usuario registrado en la plataforma PlateUp
@Entity
@Table(name = "Users")
public class User {

    // Identificador único del usuario en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Credenciales de acceso y datos básicos de la cuenta
    private String username;
    private String email;

    // La contraseña nunca se devuelve en las respuestas JSON, solo se puede escribir
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash")
    private String passwordHash;

    // Información del perfil que ven otros usuarios
    @Column(name = "display_name")
    private String displayName;

    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // Visibilidad por defecto de las recetas y colecciones que publique este usuario
    @Column(name = "visibility_default")
    private String visibilityDefault;

    // Fechas de registro y última modificación del perfil
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Racha de días consecutivos cocinando y fecha de la última actividad, usadas para los logros de constancia
    @Column(name = "streak_count")
    private Integer streakCount;

    @Column(name = "last_active_date")
    private LocalDate lastActiveDate;

    // Rol del usuario dentro de la app; por defecto es "USER", pero puede ser "ADMIN"
    @Column(name = "role")
    private String role = "USER";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getVisibilityDefault() {
        return visibilityDefault;
    }

    public void setVisibilityDefault(String visibilityDefault) {
        this.visibilityDefault = visibilityDefault;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(Integer streakCount) {
        this.streakCount = streakCount;
    }

    public LocalDate getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDate lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}