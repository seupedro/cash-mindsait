package com.example.cashflow;

import com.example.cashflow.domain.TransactionCategory;
import com.example.cashflow.domain.TransactionType;
import com.example.cashflow.dto.request.TransactionRequest;
import com.jayway.jsonpath.JsonPath;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CashFlowIntegrationCrudTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @Sql(scripts = "/init-test-db.sql")
    void createTransactionTest() throws Exception {

        // Given an input
        TransactionRequest request = TransactionRequest.builder()
                .value(BigDecimal.TEN)
                .type(TransactionType.CREDIT)
                .category(TransactionCategory.OPERATIONAL)
                .build();

        // When executed request
        ResultActions response = mockMvc.perform(post("/v1/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        // Then verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.value", is(10)))
                .andExpect(jsonPath("$.type", containsStringIgnoringCase("CREDIT")))
                .andExpect(jsonPath("$.category", containsStringIgnoringCase("OPERATIONAL")));
    }

    @Test
    @Sql(scripts = {"/init-test-db.sql", "/populate-test-db.sql"})
    void getAllTransactionTest() throws Exception {

        // When executed request
        ResultActions response = mockMvc
                .perform(get("/v1/api/transactions?date=2023-05-21")
                .contentType(MediaType.APPLICATION_JSON));

        // Then verify
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", hasSize(5)));
    }

    @Test
    @Sql(scripts = {"/init-test-db.sql", "/populate-test-db.sql"})
    void getSingleTransactionTest() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/v1/api/transactions?date=2023-05-21")
                        .contentType(MediaType.APPLICATION_JSON));


        String content = result.andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Given an id
        Object uuid = JsonPath.read(content, "$.[0].id");

        // When executed request
        ResultActions response = mockMvc
                .perform(get("/v1/api/transactions/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON));

        // Then verify that exists
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())));
    }

    @Test
    @Sql(scripts = {"/init-test-db.sql", "/populate-test-db.sql"})
    void updateSingleTransactionTest() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/v1/api/transactions?date=2023-05-21")
                        .contentType(MediaType.APPLICATION_JSON));

        String content = result.andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Given an existing id and a body
        Object uuid = JsonPath.read(content, "$.[0].id");
        TransactionRequest request = TransactionRequest.builder()
                .value(BigDecimal.ONE)
                .category(TransactionCategory.INVESTMENT)
                .type(TransactionType.DEBIT)
                .build();

        // When executed request
        ResultActions response = mockMvc
                .perform(put("/v1/api/transactions/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)));

        // Then verify that is updated
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(uuid.toString())))
                .andExpect(jsonPath("$.category", is("INVESTMENT")));
    }

    @Test
    @Sql(scripts = {"/init-test-db.sql", "/populate-test-db.sql"})
    void deleteSingleTransactionTest() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/v1/api/transactions?date=2023-05-21")
                        .contentType(MediaType.APPLICATION_JSON));


        String content = result.andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Given an id
        Object uuid = JsonPath.read(content, "$.[0].id");

        // When executed request
        ResultActions response = mockMvc
                .perform(delete("/v1/api/transactions/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON));

        // Then verify that is deleted
        response.andDo(print())
                .andExpect(status().isOk());
    }

}
