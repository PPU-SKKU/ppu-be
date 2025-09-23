package com.ppu.ppu.oppu;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "oppu", description = "오뿌 API")
@RestController
@RequestMapping("/oppu")
public class OppuController {
    OppuRepository oppuRepository;
    OppuService oppuService;

    @PostMapping("")
    public ResponseEntity<Void> postDailyRecord(@Valid @RequestBody OppuPostArticleDto post, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.postArticle(userId, post);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{oppu}")
    public ResponseEntity<OppuGetArticleDto> getDailyRecord(@PathVariable UUID oppu, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getArticle(userId, oppu));
    }

    @PutMapping("/{oppu}")
    public ResponseEntity<Void> putDailyRecord(@PathVariable("oppu") UUID oppu, @Valid @RequestBody OppuPostArticleDto post, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.putArticle(userId, oppu, post);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{oppu}")
    public ResponseEntity<Void> deleteDailyRecord(@PathVariable UUID oppu, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuService.deleteArticle(userId, oppu);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<> getDailyRecords(@RequestParam int date, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getDailyArticles(userId, date));
    }

    @GetMapping("/month")
    public ResponseEntity<> getMonthlyRecords(@RequestParam int date, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuService.getMonthlyTags(userId, date));
    }


}
