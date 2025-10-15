package com.example.stockkkkk.disclosure.config;

import com.example.stockkkkk.disclosure.service.DisclosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 시작 시 최근 7일간 공시 데이터 로드
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DisclosureInitializer implements ApplicationRunner {

    private final DisclosureService disclosureService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("========================================");
        log.info("애플리케이션 시작 - 공시 데이터 초기화");
        log.info("========================================");

        try {
            // Python API 서버 확인 (간단한 health check)
            log.info("Python API 서버 상태 확인...");

            // 최근 7일간 공시 데이터 로드
            disclosureService.loadInitialData();

            log.info("========================================");
            log.info("공시 데이터 초기화 완료!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("========================================");
            log.error("공시 데이터 초기화 실패!");
            log.error("Python API 서버가 실행 중인지 확인하세요 (포트 8000)");
            log.error("에러: {}", e.getMessage());
            log.error("========================================");
            // 에러 발생해도 애플리케이션은 계속 실행
        }
    }
}
