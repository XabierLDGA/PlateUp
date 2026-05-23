package es.ieslavereda.plateup.dto;

// Datos que el usuario envía para iniciar sesión
public class LoginRequest {
    // El identificador puede ser el nombre de usuario o el correo electrónico
    private String identifier;
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}