package com.ppu.ppu.user.perfume;

import com.ppu.ppu.user.perfume.have.UserPerfumeHaveService;
import com.ppu.ppu.user.perfume.wish.UserPerfumeWishService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/perfume")
public class UserPerfumeController {
    private final UserPerfumeWishService userPerfumeWishService;
    private final UserPerfumeHaveService userPerfumeHaveService;

    @GetMapping("/wish")
    public ResponseEntity<UserPerfumeDto> getWish(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        List<UUID> perfumeIds = userPerfumeWishService
                .findPerfumeIdsByUserId(userId)
                .orElse(Collections.emptyList());

        return ResponseEntity.ok(new UserPerfumeDto(perfumeIds));
    }

    @PostMapping("/wish")
    public ResponseEntity<Void> postWish(@Valid @RequestBody UserPerfumeDto perfumeIds, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userPerfumeWishService.addPerfumeIdsByUserIdAndPerfumeIds(userId, perfumeIds.getPerfumeIds());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/wish")
    public ResponseEntity<Void> deleteWish(@Valid @RequestBody UserPerfumeDto perfumeIds, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userPerfumeWishService.deleteByUserIdAndPerfumeIdIn(userId, perfumeIds.getPerfumeIds());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/have")
    public ResponseEntity<UserPerfumeDto> getHave(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        List<UUID> perfumeIds = userPerfumeHaveService
                .findPerfumeIdsByUserId(userId)
                .orElse(Collections.emptyList());

        return ResponseEntity.ok(new UserPerfumeDto(perfumeIds));
    }

    @PostMapping("/have")
    public ResponseEntity<Void> postHave(@Valid @RequestBody UserPerfumeDto perfumeIds, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userPerfumeHaveService.addPerfumeIdsByUserIdAndPerfumeIds(userId, perfumeIds.getPerfumeIds());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/have")
    public ResponseEntity<Void> deleteHave(@Valid @RequestBody UserPerfumeDto perfumeIds, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userPerfumeHaveService.deleteByUserIdAndPerfumeIdIn(userId, perfumeIds.getPerfumeIds());
        return ResponseEntity.ok().build();
    }
}
