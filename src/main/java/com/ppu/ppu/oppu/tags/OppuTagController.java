package com.ppu.ppu.oppu.tags;

import com.ppu.ppu.oppu.tags.dto.OppuTagCreateDto;
import com.ppu.ppu.oppu.tags.dto.OppuTagsResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "oppu tag", description = "오뿌 태그 API")
@RestController
@RequestMapping("/oppu/tags")
@AllArgsConstructor
public class OppuTagController {
    private final OppuTagService oppuTagService;

    @PostMapping("")
    public ResponseEntity<Void> postTag(@Valid @RequestBody OppuTagCreateDto dto, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuTagService.createTag(userId, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putTag(@Parameter(description = "oppu tag ID", required = true) @PathVariable("id") UUID id, @Valid @RequestBody OppuTagCreateDto dto, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuTagService.replaceTag(userId, id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@Parameter(description = "oppu tag ID", required = true) @PathVariable("id") UUID id, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        oppuTagService.deleteTag(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<OppuTagsResponseDto> getTag(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        return ResponseEntity.ok(oppuTagService.getTags(userId));
    }

}
