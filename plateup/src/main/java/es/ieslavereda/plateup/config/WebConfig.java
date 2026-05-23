package es.ieslavereda.plateup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Directorio del sistema de archivos donde se encuentran las imágenes subidas
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // Mapea las peticiones a /uploads/** al directorio físico del servidor para servir imágenes estáticamente
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        // Convertimos la ruta a URI para que Spring la pueda usar como ubicación de recursos
        String uploadLocation = uploadPath.toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);
    }
}