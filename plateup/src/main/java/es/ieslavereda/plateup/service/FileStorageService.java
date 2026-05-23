package es.ieslavereda.plateup.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    // Solo se aceptan estos formatos de imagen para las subidas de archivos
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    // Directorio raíz donde se guardan las imágenes, configurable por variable de entorno
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path uploadsRoot;

    // Crea las carpetas necesarias al arrancar la aplicación si no existen
    @PostConstruct
    public void init() throws IOException {
        uploadsRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadsRoot.resolve("avatars"));
        Files.createDirectories(uploadsRoot.resolve("recipes"));
    }

    // Guarda la imagen de perfil de un usuario en la carpeta de avatares
    public String storeAvatar(MultipartFile file) {
        return storeImage(file, "avatars", "avatar");
    }

    // Guarda la imagen de una receta en la carpeta correspondiente
    public String storeRecipeImage(MultipartFile file) {
        return storeImage(file, "recipes", "recipe");
    }

    // Comprueba si una ruta pertenece a nuestro sistema de subidas gestionado
    public boolean isManagedUploadPath(String value) {
        return value != null && value.startsWith("/uploads/");
    }

    // Elimina un archivo del disco solo si es una ruta gestionada por la aplicación
    public void deleteIfManagedPath(String publicPath) {
        if (!isManagedUploadPath(publicPath)) {
            return;
        }

        try {
            String relativePath = publicPath.replaceFirst("^/uploads/?", "");
            Path target = uploadsRoot.resolve(relativePath).normalize();

            // Verificamos que la ruta resultante sigue dentro del directorio de subidas para evitar path traversal
            if (!target.startsWith(uploadsRoot)) {
                return;
            }

            Files.deleteIfExists(target);
        } catch (Exception e) {
            log.warn("Failed to delete file {}: {}", publicPath, e.getMessage());
        }
    }

    // Valida la imagen, genera un nombre único y la guarda en el subdirectorio indicado
    private String storeImage(MultipartFile file, String folder, String prefix) {
        validateImage(file);

        try {
            String extension = resolveExtension(file);
            // Usamos UUID para que el nombre del archivo sea único y no haya colisiones
            String filename = prefix + "-" + UUID.randomUUID() + "." + extension;

            Path folderPath = uploadsRoot.resolve(folder).normalize();
            Files.createDirectories(folderPath);

            Path target = folderPath.resolve(filename).normalize();

            // Segunda comprobación de seguridad contra path traversal antes de escribir
            if (!target.startsWith(folderPath)) {
                throw new IllegalArgumentException("Invalid file path.");
            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + folder + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("The file could not be saved.", e);
        }
    }

    // Lanza una excepción si el archivo es nulo, está vacío o tiene un tipo de contenido no permitido
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("You must send an image file.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only JPG, PNG and WEBP images are allowed.");
        }
    }

    // Determina la extensión correcta a partir del nombre original del archivo o, si falla, del content type
    private String resolveExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

            if (extension.equals("jpg") || extension.equals("jpeg")) {
                return "jpg";
            }

            if (extension.equals("png")) {
                return "png";
            }

            if (extension.equals("webp")) {
                return "webp";
            }
        }

        // Si el nombre del archivo no tiene extensión reconocible, usamos el content type como respaldo
        String contentType = file.getContentType();
        if ("image/png".equalsIgnoreCase(contentType)) {
            return "png";
        }

        if ("image/webp".equalsIgnoreCase(contentType)) {
            return "webp";
        }

        return "jpg";
    }
}