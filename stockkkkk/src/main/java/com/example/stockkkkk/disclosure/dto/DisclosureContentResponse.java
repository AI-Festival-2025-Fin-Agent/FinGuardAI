package com.example.stockkkkk.disclosure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisclosureContentResponse {
    private String rceptNo;
    private String company;
    private String report;
    private String content;
    private Integer contentLength;
}
