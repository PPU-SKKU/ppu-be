package com.ppu.ppu.utils.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "Image", description = "Image S3 API (Test only)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("")
    public ResponseEntity<Void> uploadImage(@RequestParam("file") MultipartFile file) {
        imageService.uploadImage(UUID.fromString("41d0ec65-2885-4966-92d1-255bafc4467f"), "ppubucket", "dir", file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/replace")
    public ResponseEntity<Void> replaceImage(@RequestParam("image_id") UUID imageId, @RequestParam("file") MultipartFile file) {
        imageService.replaceOwnerImage(UUID.fromString("41d0ec65-2885-4966-92d1-255bafc4467f"), imageId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteImage(@RequestParam("image_id") UUID imageId) {
        imageService.deleteOwnerImage(UUID.fromString("41d0ec65-2885-4966-92d1-255bafc4467f"), imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getDownloadUrl(
            @PathVariable UUID id
    ) {
        URL url = imageService.getDownloadUrl(id);
        return ResponseEntity.ok(url.toString());
    }
}
