package com.example.stockkkkk.disclosure.service;

import com.example.stockkkkk.disclosure.domain.Disclosure;
import com.example.stockkkkk.disclosure.dto.DartApiDisclosureDto;
import com.example.stockkkkk.disclosure.dto.DisclosureContentResponse;
import com.example.stockkkkk.disclosure.dto.DisclosureResponse;
import com.example.stockkkkk.disclosure.repository.DisclosureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisclosureService {

    private final DisclosureRepository disclosureRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PYTHON_API_URL = "http://localhost:8000/api/disclosures";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    /**
     * 초기 데이터 로드: 최근 7일간 공시 가져오기
     */
    @Transactional
    public void loadInitialData() {
        log.info("=== 초기 데이터 로딩 시작 ===");
        fetchAndSaveDisclosures(7);
        log.info("=== 초기 데이터 로딩 완료 ===");
    }

    /**
     * 스케줄러: 당일 공시 30분마다 갱신
     */
    @Scheduled(fixedRate = 1800000) // 30분 = 1800000ms
    @Transactional
    public void refreshTodayDisclosures() {
        log.info("=== 당일 공시 갱신 시작 ===");
        fetchAndSaveDisclosures(1);
        log.info("=== 당일 공시 갱신 완료 ===");
    }

    /**
     * 스케줄러: 7일 이전 데이터 삭제 (매일 자정)
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanOldData() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Disclosure> oldDisclosures = disclosureRepository.findByDisclosureDateBefore(sevenDaysAgo);

        if (!oldDisclosures.isEmpty()) {
            disclosureRepository.deleteAll(oldDisclosures);
            log.info("=== {} 이전 데이터 {}건 삭제 ===", sevenDaysAgo, oldDisclosures.size());
        }
    }

    /**
     * Python API로부터 공시 데이터 가져와서 DB에 저장
     */
    private void fetchAndSaveDisclosures(int mdayCnt) {
        try {
            String url = PYTHON_API_URL + "/recent?mday_cnt=" + mdayCnt + "&fetch_all=true";
            log.info("Python API 호출: {}", url);

            ResponseEntity<List<DartApiDisclosureDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DartApiDisclosureDto>>() {}
            );

            List<DartApiDisclosureDto> disclosures = response.getBody();
            if (disclosures == null || disclosures.isEmpty()) {
                log.warn("Python API로부터 데이터를 받지 못했습니다.");
                return;
            }

            log.info("Python API로부터 {}건의 공시 데이터 수신", disclosures.size());

            int newCount = 0;
            for (DartApiDisclosureDto dto : disclosures) {
                if (!disclosureRepository.existsByRceptNo(dto.getRceptNo())) {
                    Disclosure disclosure = convertToEntity(dto);
                    disclosureRepository.save(disclosure);
                    newCount++;
                }
            }

            log.info("{}건의 새로운 공시 저장 완료", newCount);

        } catch (Exception e) {
            log.error("공시 데이터 가져오기 실패", e);
        }
    }

    /**
     * DTO를 Entity로 변환
     */
    private Disclosure convertToEntity(DartApiDisclosureDto dto) {
        LocalDate disclosureDate = LocalDate.parse(dto.getDate(), DATE_FORMATTER);

        return Disclosure.builder()
                .rceptNo(dto.getRceptNo())
                .company(dto.getCompany())
                .report(dto.getReport())
                .submitter(dto.getSubmitter())
                .disclosureDate(disclosureDate)
                .disclosureTime(dto.getTime())
                .market(dto.getMarket())
                .url(dto.getUrl())
                .isCorrection(dto.getIsCorrection())
                .build();
    }

    /**
     * 특정 날짜의 공시 목록 조회
     */
    @Transactional(readOnly = true)
    public List<DisclosureResponse> getDisclosuresByDate(LocalDate date) {
        List<Disclosure> disclosures = disclosureRepository.findByDisclosureDateOrderByDisclosureTimeDesc(date);
        return disclosures.stream()
                .map(DisclosureResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 최근 7일간 날짜별 공시 개수
     */
    @Transactional(readOnly = true)
    public Map<LocalDate, Long> getRecentDisclosureCountsByDate() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        List<Disclosure> disclosures = disclosureRepository.findRecentDisclosures(sevenDaysAgo);

        return disclosures.stream()
                .collect(Collectors.groupingBy(Disclosure::getDisclosureDate, Collectors.counting()));
    }

    /**
     * 특정 공시의 내용 가져오기
     */
    @Transactional
    public DisclosureContentResponse getDisclosureContent(String rceptNo) {
        Disclosure disclosure = disclosureRepository.findByRceptNo(rceptNo)
                .orElseThrow(() -> new RuntimeException("공시를 찾을 수 없습니다: " + rceptNo));

        // 이미 내용이 있으면 DB에서 반환
        if (disclosure.getContentFetched() && disclosure.getContent() != null) {
            log.info("DB에서 공시 내용 반환: {}", rceptNo);
            return DisclosureContentResponse.builder()
                    .rceptNo(disclosure.getRceptNo())
                    .company(disclosure.getCompany())
                    .report(disclosure.getReport())
                    .content(disclosure.getContent())
                    .contentLength(disclosure.getContent().length())
                    .build();
        }

        // Python API로 내용 가져오기
        try {
            String url = PYTHON_API_URL + "/" + rceptNo + "/content";
            log.info("Python API 호출 (내용): {}", url);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> contentData = response.getBody();
            if (contentData != null && contentData.containsKey("content")) {
                String content = (String) contentData.get("content");

                // DB에 저장
                disclosure.updateContent(content);
                disclosureRepository.save(disclosure);

                log.info("공시 내용 저장 완료: {} ({} 문자)", rceptNo, content.length());

                return DisclosureContentResponse.builder()
                        .rceptNo(disclosure.getRceptNo())
                        .company(disclosure.getCompany())
                        .report(disclosure.getReport())
                        .content(content)
                        .contentLength(content.length())
                        .build();
            }

            throw new RuntimeException("Python API로부터 내용을 가져오지 못했습니다.");

        } catch (Exception e) {
            log.error("공시 내용 가져오기 실패: {}", rceptNo, e);
            throw new RuntimeException("공시 내용 가져오기 실패", e);
        }
    }
}
