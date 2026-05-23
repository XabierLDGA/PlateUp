package es.ieslavereda.plateup.dto;

import es.ieslavereda.plateup.model.User;

// Respuesta que se devuelve al cliente cuando el login o el registro son exitosos
public class AuthResponse {

    // El token JWT que el cliente debe incluir en las siguientes peticiones
    private String token;
    // Datos completos del usuario para que el frontend pueda mostrarlos sin otra llamada a la API
    private User user;

    public AuthResponse() {
    }

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}