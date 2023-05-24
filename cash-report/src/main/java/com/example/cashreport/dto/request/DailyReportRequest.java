package com.example.cashreport.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyReportRequest {

//    @Accessors(fluent = true)
//    @Getter(onMethod = @__(@JsonProperty))
    private Boolean executeTask = false;
    private LocalDate reportDate = LocalDate.now();


}
