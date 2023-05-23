package com.example.cashreport.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportRequest {

    @Accessors(fluent = true)
    @Getter(onMethod = @__(@JsonProperty))
    private Boolean executeTask = false;
    private LocalDate reportDate = LocalDate.now();


}
