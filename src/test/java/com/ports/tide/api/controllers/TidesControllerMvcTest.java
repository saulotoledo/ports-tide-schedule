package com.ports.tide.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import com.ports.tide.api.services.TidesService;
import com.ports.tide.generators.TidesGenerator;
import com.ports.tide.projections.implementations.TideSummaryEntryImpl;
import com.ports.tide.projections.implementations.WeeklyReportEntryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TidesController.class)
@DisplayName("Integration tests for the tide controller with the MVC layer")
public class TidesControllerMvcTest {

    public static final String CONTENT_TYPE = "Content-Type";

    @MockBean
    private TidesService tidesService;

    @SpyBean
    private TidesController tidesController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private <T> List<T> convertCollectionJsonNodeResultContentToEntityArray(
        JsonNode contentJsonNode, Class<T> valueType
    ) throws IOException {
        List<T> resultList = new ArrayList<>();

        Iterator<JsonNode> elementsIterator = contentJsonNode.elements();
        while (elementsIterator.hasNext()) {
            resultList.add(
                this.objectMapper.treeToValue(elementsIterator.next(), valueType)
            );
        }

        return resultList;
    }

    @Test
    @DisplayName("Should return the statistics retrieved by the service layer as JSON in a page with HTTP status 200 OK")
    public void testGetStatistics() throws Exception {
        TidesStatistics stats = TidesGenerator.generateTidesStatistics();
        when(this.tidesService.getStatistics()).thenReturn(stats);

        MvcResult result = mockMvc.perform(get("/tides/stats"))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode responseContentJsonNode = this.objectMapper.readTree(responseContent);

        assertThat(responseContentJsonNode.has("low"), is(true));
        assertThat(responseContentJsonNode.get("low").has("amplitude"), is(true));
        assertThat(responseContentJsonNode.get("low").get("amplitude").has("average"), is(true));
        assertThat(
            responseContentJsonNode.get("low").get("amplitude").get("average").floatValue(),
            is(stats.getLow().getAmplitude().getAverage())
        );
        assertThat(responseContentJsonNode.has("high"), is(true));
        assertThat(responseContentJsonNode.get("high").has("amplitude"), is(true));
        assertThat(responseContentJsonNode.get("high").get("amplitude").has("average"), is(true));
        assertThat(
            responseContentJsonNode.get("high").get("amplitude").get("average").floatValue(),
            is(stats.getHigh().getAmplitude().getAverage())
        );
        assertThat(responseContentJsonNode.get("high").has("forShipments"), is(true));
        assertThat(responseContentJsonNode.get("high").get("forShipments").has("total"), is(true));
        assertThat(
            responseContentJsonNode.get("high").get("forShipments").get("total").intValue(),
            is(stats.getHigh().getForShipments().getTotal())
        );
        assertThat(responseContentJsonNode.get("high").get("forShipments").has("assigned"), is(true));
        assertThat(
            responseContentJsonNode.get("high").get("forShipments").get("assigned").intValue(),
            is(stats.getHigh().getForShipments().getAssigned())
        );
    }

    @Test
    @DisplayName("Should receive pagination parameters on paginated requests")
    void testPaginationParametersReceived() {
        Integer page = 0;
        Integer size = 2;

        Arrays.asList("/tides/summary", "/tides/report").forEach(url -> {
            try {
                mockMvc
                    .perform(get(url)
                        .param("page", page.toString())
                        .param("size", size.toString())
                    )
                    .andExpect(status().isOk())
                    .andReturn();

                ArgumentCaptor<Pageable> pageableRequestArgument = ArgumentCaptor.forClass(Pageable.class);
                Mockito.verify(this.tidesController).getSummary(pageableRequestArgument.capture());

                Pageable pageableRequestInstance = pageableRequestArgument.getValue();
                assertThat(pageableRequestInstance.getPageNumber(), is(page));
                assertThat(pageableRequestInstance.getPageSize(), is(size));
            } catch (Exception e) {
                fail(e.getMessage());
            }
        });
    }
}
