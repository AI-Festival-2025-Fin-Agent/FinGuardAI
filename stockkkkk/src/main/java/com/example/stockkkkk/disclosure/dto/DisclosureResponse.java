package com.example.stockkkkk.disclosure.dto;

import com.example.stockkkkk.disclosure.domain.Disclosure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisclosureResponse {
    private Long id;
    private String rceptNo;
    private String company;
    private String report;
    private String submitter;
    private LocalDate disclosureDate;
    private String disclosureTime;
    private String market;
    private String url;
    private Boolean isCorrection;
    private Boolean contentFetched;

    public static DisclosureResponse from(Disclosure disclosure) {
        return DisclosureResponse.builder()
                .id(disclosure.getId())
                .rceptNo(disclosure.getRceptNo())
                .company(disclosure.getCompany())
                .report(disclosure.getReport())
                .submitter(disclosure.getSubmitter())
                .disclosureDate(disclosure.getDisclosureDate())
                .disclosureTime(disclosure.getDisclosureTime())
                .market(disclosure.getMarket())
                .url(disclosure.getUrl())
                .isCorrection(disclosure.getIsCorrection())
                .contentFetched(disclosure.getContentFetched())
                .build();
    }
}
