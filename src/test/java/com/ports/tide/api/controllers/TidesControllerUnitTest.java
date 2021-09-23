package com.ports.tide.api.controllers;

import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import com.ports.tide.api.services.TidesService;
import com.ports.tide.generators.TidesGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for the tide controller")
public class TidesControllerUnitTest {

    @Mock
    private TidesService tidesService;

    @InjectMocks
    private TidesController tidesController;

    @Test
    @DisplayName("Should return the summary retrieved by the service layer")
    public void testGetSummaryFromService() {
        Pageable pageable = PageRequest.of(0, 5);
        List<TideSummaryEntry> entries = TidesGenerator.generateSummary(5);
        when(tidesService.getSummary(any(Pageable.class))).thenReturn(
            new PageImpl<>(entries, pageable, entries.size())
        );

        Page<TideSummaryEntry> result = this.tidesController.getSummary(pageable);

        assertThat(result.getContent(), is(entries));
    }

    @Test
    @DisplayName("Should return the report retrieved by the service layer")
    public void testGetWeeklyReportFromService() {
        Pageable pageable = PageRequest.of(0, 5);
        List<WeeklyReportEntry> entries = TidesGenerator.generateWeeklyReport(5);
        when(tidesService.getWeeklyReport(any(Pageable.class))).thenReturn(
            new PageImpl<>(entries, pageable, entries.size())
        );

        Page<WeeklyReportEntry> result = this.tidesController.getReport(pageable);

        assertThat(result.getContent(), is(entries));
    }

    @Test
    @DisplayName("Should return the statistics retrieved by the service layer")
    public void testGetStatisticsFromService() {
        TidesStatistics stats = TidesGenerator.generateTidesStatistics();
        when(tidesService.getStatistics()).thenReturn(stats);

        TidesStatistics result = this.tidesController.getStatistics();

        assertThat(result, is(stats));
    }
}
