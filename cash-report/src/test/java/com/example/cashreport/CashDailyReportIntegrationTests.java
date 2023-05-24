package com.example.cashreport;


import com.example.cashreport.model.DailyReport;
import com.example.cashreport.repository.DailyReportRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CashDailyReportIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DailyReportRepository repository;

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @Sql(scripts = "/init-test-db.sql")
    void createAndGetReportTest() throws Exception {
        DailyReport report = DailyReport.builder()
                .reportDate(LocalDate.now())
                .countOfCredits(1L)
                .countOfDebits(0L)
                .totalCredit(BigDecimal.TEN)
                .totalDebit(BigDecimal.ONE)
                .totalBalance(new BigDecimal("11"))
                .build();

        repository.save(report);

        ResultActions result = mockMvc
                .perform(get("/v1/api/cashreport")
                        .contentType(MediaType.APPLICATION_JSON));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].report_date", is(LocalDate.now().toString())));

    }
}
