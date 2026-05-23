package es.ieslavereda.plateup.dto;

// Datos que el usuario envía al crear una nueva cuenta en PlateUp
public class RegisterRequest {
    // Campos obligatorios para crear la cuenta
    private String username;
    private String email;
    private String password;
    // Campos opcionales del perfil que el usuario puede rellenar al registrarse
    private String displayName;
    private String bio;
    private String avatarUrl;
    // Visibilidad por defecto de las recetas que publique el usuario (por ejemplo, "public" o "private")
    private String visibilityDefault;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}