package com.example.stockkkkk.disclosure.controller;

import com.example.stockkkkk.disclosure.dto.DisclosureContentResponse;
import com.example.stockkkkk.disclosure.dto.DisclosureResponse;
import com.example.stockkkkk.disclosure.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/disclosures")
@RequiredArgsConstructor
public class DisclosureController {

    private final DisclosureService disclosureService;

    /**
     * 최근 7일간 날짜별 공시 개수
     * GET /api/disclosures/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<LocalDate, Long>> getDisclosureSummary() {
        Map<LocalDate, Long> summary = disclosureService.getRecentDisclosureCountsByDate();
        return ResponseEntity.ok(summary);
    }

    /**
     * 특정 날짜의 공시 목록 조회
     * GET /api/disclosures?date=2025-10-11
     */
    @GetMapping
    public ResponseEntity<List<DisclosureResponse>> getDisclosuresByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<DisclosureResponse> disclosures = disclosureService.getDisclosuresByDate(date);
        return ResponseEntity.ok(disclosures);
    }

    /**
     * 특정 공시의 내용 조회
     * GET /api/disclosures/{rceptNo}/content
     */
    @GetMapping("/{rceptNo}/content")
    public ResponseEntity<DisclosureContentResponse> getDisclosureContent(
            @PathVariable String rceptNo
    ) {
        DisclosureContentResponse content = disclosureService.getDisclosureContent(rceptNo);
        return ResponseEntity.ok(content);
    }

    /**
     * 수동 데이터 갱신 (테스트용)
     * POST /api/disclosures/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshDisclosures() {
        disclosureService.loadInitialData();
        return ResponseEntity.ok("데이터 갱신 완료");
    }
}
