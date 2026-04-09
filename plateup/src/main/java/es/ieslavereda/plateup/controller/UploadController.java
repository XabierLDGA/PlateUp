package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "*")
public class UploadController {

    private final FileStorageService fileStorageService;

    public UploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return upload(file, true);
    }

    @PostMapping(value = "/recipe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadRecipe(@RequestPart("file") MultipartFile file) {
        return upload(file, false);
    }

    private ResponseEntity<?> upload(MultipartFile file, boolean avatar) {
        try {
            String path = avatar
                    ? fileStorageService.storeAvatar(file)
                    : fileStorageService.storeRecipeImage(file);

            Map<String, String> body = new HashMap<>();
            body.put("path", path);

            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error("The file could not be uploaded."));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}