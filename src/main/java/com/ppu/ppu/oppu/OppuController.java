package com.ppu.ppu.oppu;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "oppu", description = "오뿌 API")
@RestController
@RequestMapping("/oppu")
@AllArgsConstructor
public class OppuController {
    private final OppuService oppuService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> postArticle(
            @Valid @RequestPart("post") OppuPostArticleDto post,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.postArticle(userId, post, images);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OppuGetArticleDto> getArticle(
            @PathVariable("id") UUID id,
            HttpServletRequest request)
    {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getArticle(userId, id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> putArticle(
            @PathVariable("id") UUID id,
            @Valid @RequestPart("post") OppuPostArticleDto post,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request) throws ServletException, IOException {

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.putArticle(userId, id, post, images);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") UUID id, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.deleteArticle(userId, id);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/daily")
    public ResponseEntity<> getDailyRecords(@RequestParam int date, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getDailyArticles(userId, date));
    }

    @GetMapping("/month")
    public ResponseEntity<> getMonthlyRecords(@RequestParam int date, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getMonthlyTags(userId, date));
    }*/


}
