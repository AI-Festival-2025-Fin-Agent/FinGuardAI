package com.example.stockkkkk.disclosure.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "disclosures", indexes = {
    @Index(name = "idx_disclosure_date", columnList = "disclosureDate"),
    @Index(name = "idx_rcept_no", columnList = "rceptNo", unique = true),
    @Index(name = "idx_company", columnList = "company")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Disclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String rceptNo;  // 접수번호

    @Column(nullable = false, length = 200)
    private String company;  // 회사명

    @Column(nullable = false, length = 500)
    private String report;  // 보고서명

    @Column(nullable = false, length = 200)
    private String submitter;  // 제출인

    @Column(nullable = false)
    private LocalDate disclosureDate;  // 공시일자

    @Column(length = 10)
    private String disclosureTime;  // 공시시간

    @Column(length = 10)
    private String market;  // 시장구분 (유/코/공)

    @Column(nullable = false, length = 500)
    private String url;  // 공시 URL

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCorrection = false;  // 정정공시 여부

    @Column(columnDefinition = "TEXT")
    private String content;  // 공시 내용 (옵션)

    @Column
    @Builder.Default
    private Boolean contentFetched = false;  // 내용 조회 여부

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 비즈니스 로직
    public void updateContent(String content) {
        this.content = content;
        this.contentFetched = true;
    }

    public boolean isToday() {
        return this.disclosureDate.equals(LocalDate.now());
    }
}
