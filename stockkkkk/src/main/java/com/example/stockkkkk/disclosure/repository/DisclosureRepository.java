package com.example.stockkkkk.disclosure.repository;

import com.example.stockkkkk.disclosure.domain.Disclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {

    /**
     * 접수번호로 공시 조회
     */
    Optional<Disclosure> findByRceptNo(String rceptNo);

    /**
     * 특정 날짜의 공시 목록 조회
     */
    List<Disclosure> findByDisclosureDateOrderByDisclosureTimeDesc(LocalDate date);

    /**
     * 날짜 범위로 공시 목록 조회
     */
    @Query("SELECT d FROM Disclosure d WHERE d.disclosureDate BETWEEN :startDate AND :endDate ORDER BY d.disclosureDate DESC, d.disclosureTime DESC")
    List<Disclosure> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 최근 N일간 공시 목록 조회
     */
    @Query("SELECT d FROM Disclosure d WHERE d.disclosureDate >= :fromDate ORDER BY d.disclosureDate DESC, d.disclosureTime DESC")
    List<Disclosure> findRecentDisclosures(@Param("fromDate") LocalDate fromDate);

    /**
     * 특정 날짜 이전 공시 삭제용 조회
     */
    List<Disclosure> findByDisclosureDateBefore(LocalDate date);

    /**
     * 접수번호가 이미 존재하는지 확인
     */
    boolean existsByRceptNo(String rceptNo);

    /**
     * 회사명으로 최근 공시 조회
     */
    @Query("SELECT d FROM Disclosure d WHERE d.company LIKE %:companyName% AND d.disclosureDate >= :fromDate ORDER BY d.disclosureDate DESC, d.disclosureTime DESC")
    List<Disclosure> findByCompanyNameAndDateAfter(@Param("companyName") String companyName, @Param("fromDate") LocalDate fromDate);
}
