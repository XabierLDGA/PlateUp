package es.ieslavereda.plateup.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path uploadsRoot;

    @PostConstruct
    public void init() throws IOException {
        uploadsRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadsRoot.resolve("avatars"));
        Files.createDirectories(uploadsRoot.resolve("recipes"));
    }

    public String storeAvatar(MultipartFile file) {
        return storeImage(file, "avatars", "avatar");
    }

    public String storeRecipeImage(MultipartFile file) {
        return storeImage(file, "recipes", "recipe");
    }

    public boolean isManagedUploadPath(String value) {
        return value != null && value.startsWith("/uploads/");
    }

    public void deleteIfManagedPath(String publicPath) {
        if (!isManagedUploadPath(publicPath)) {
            return;
        }

        try {
            String relativePath = publicPath.replaceFirst("^/uploads/?", "");
            Path target = uploadsRoot.resolve(relativePath).normalize();

            if (!target.startsWith(uploadsRoot)) {
                return;
            }

            Files.deleteIfExists(target);
        } catch (Exception ignored) {
        }
    }

    private String storeImage(MultipartFile file, String folder, String prefix) {
        validateImage(file);

        try {
            String extension = resolveExtension(file);
            String filename = prefix + "-" + UUID.randomUUID() + "." + extension;

            Path folderPath = uploadsRoot.resolve(folder).normalize();
            Files.createDirectories(folderPath);

            Path target = folderPath.resolve(filename).normalize();

            if (!target.startsWith(folderPath)) {
                throw new IllegalArgumentException("Invalid file path.");
            }

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + folder + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("The file could not be saved.", e);
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("You must send an image file.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only JPG, PNG and WEBP images are allowed.");
        }
    }

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