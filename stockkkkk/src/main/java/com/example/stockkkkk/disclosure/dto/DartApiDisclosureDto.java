package com.example.stockkkkk.disclosure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Python DART API로부터 받는 공시 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DartApiDisclosureDto {

    @JsonProperty("number")
    private String number;

    @JsonProperty("company")
    private String company;

    @JsonProperty("report")
    private String report;

    @JsonProperty("submitter")
    private String submitter;

    @JsonProperty("date")
    private String date;  // YYYY.MM.DD 형식

    @JsonProperty("time")
    private String time;

    @JsonProperty("market")
    private String market;

    @JsonProperty("rcept_no")
    private String rceptNo;

    @JsonProperty("url")
    private String url;

    @JsonProperty("is_correction")
    private Boolean isCorrection;
}
