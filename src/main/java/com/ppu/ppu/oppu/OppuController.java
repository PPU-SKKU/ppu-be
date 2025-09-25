package com.ppu.ppu.oppu;

import com.ppu.ppu.oppu.dto.OppuDailyResponseDto;
import com.ppu.ppu.oppu.dto.OppuResponseDto;
import com.ppu.ppu.oppu.dto.OppuMonthlyResponseDto;
import com.ppu.ppu.oppu.dto.OppuCreateDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @Valid @RequestPart("post") OppuCreateDto post,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.postArticle(userId, post, images);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OppuResponseDto> getArticle(
            @Parameter(description = "article ID", required = true) @PathVariable("id") UUID id,
            HttpServletRequest request)
    {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getArticle(userId, id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> putArticle(
            @Parameter(description = "article ID", required = true) @PathVariable("id") UUID id,
            @Valid @RequestPart("post") OppuCreateDto post,
            @RequestPart(name = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request) {

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.putArticle(userId, id, post, images);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@Parameter(description = "article ID", required = true) @PathVariable("id") UUID id, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.deleteArticle(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<OppuDailyResponseDto> getDailyRecords(
            @RequestParam(name = "date", required = true)
            @Pattern(regexp = "^(\\d{4})(0[1-9]|1[0-2])(\\d{2})$", message = "pattern should be YYYYMMDD(ex. 20250903)")
            String date, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getDailyArticles(userId, date));
    }

    @GetMapping("/month")
    public ResponseEntity<OppuMonthlyResponseDto> getMonthlyRecords(
            @RequestParam(name = "month", required = true)
            @Pattern(regexp = "^(\\d{4})(0[1-9]|1[0-2])$", message = "pattern should be YYYYMM(ex. 202509)")
            String date,

            HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getMonthlyTags(userId, date));
    }
}
