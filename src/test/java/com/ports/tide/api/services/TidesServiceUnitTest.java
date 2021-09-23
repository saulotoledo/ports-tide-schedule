package com.ports.tide.api.services;

import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.enums.TideType;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.TidesGeneralStatisticsEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import com.ports.tide.api.repositories.TidesRepository;
import com.ports.tide.generators.TidesGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SuppressWarnings("ALL")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for the tides service")
public class TidesServiceUnitTest {

    @Mock
    private TidesRepository tidesRepository;

    @InjectMocks
    private TidesService tidesService;

    @Test
    @DisplayName("Should return a summary of tides")
    public void testGetSummary() {
        Pageable pageable = PageRequest.of(0, 5);
        List<TideSummaryEntry> allEntries = TidesGenerator.generateSummary(5);
        when(tidesRepository.findSummaryTides(any(Pageable.class))).thenReturn(
            new PageImpl<>(allEntries, pageable, allEntries.size())
        );

        Page<TideSummaryEntry> result = this.tidesService.getSummary(pageable);

        assertThat(result.getContent(), containsInAnyOrder(allEntries.toArray()));
    }

    @Test
    @DisplayName("Should return a weekly report of tides")
    public void testGetWeeklyReport() {
        Pageable pageable = PageRequest.of(0, 5);
        List<WeeklyReportEntry> allEntries = TidesGenerator.generateWeeklyReport(5);
        when(tidesRepository.findWeeklyReport(any(Pageable.class))).thenReturn(
            new PageImpl<>(allEntries, pageable, allEntries.size())
        );

        Page<WeeklyReportEntry> result = this.tidesService.getWeeklyReport(pageable);

        assertThat(result.getContent(), containsInAnyOrder(allEntries.toArray()));
    }

    @Test
    @DisplayName("Should return a statistics object")
    public void testGetStatistics() {
        TidesGeneralStatisticsEntry lowTideStats = TidesGenerator.generateTidesGeneralStatisticsEntry(TideType.LOW);
        TidesGeneralStatisticsEntry highTideStats = TidesGenerator.generateTidesGeneralStatisticsEntry(TideType.HIGH);

        List<TidesGeneralStatisticsEntry> tidesGeneralStatistics = Arrays.asList(lowTideStats, highTideStats);

        Integer tidesAboveThreshold = 30;
        Integer tidesAboveThresholdAssignedForShipments = 12;
        when(tidesRepository.computeGeneralStatistics()).thenReturn(tidesGeneralStatistics);

        when(tidesRepository.countByTideTypeWithAmplitudeGreaterThan(any(TideType.class), nullable(Float.class)))
            .thenReturn(tidesAboveThreshold);
        when(tidesRepository.countAssignedShipmentsByTideTypeWithAmplitudeGreaterThan(
            any(TideType.class), nullable(Float.class))
        ).thenReturn(tidesAboveThresholdAssignedForShipments);

        TidesStatistics result = this.tidesService.getStatistics();

        assertThat(result.getLow().getAmplitude().getAverage(), is(lowTideStats.getAmplitudeAverage()));
        assertThat(result.getHigh().getAmplitude().getAverage(), is(highTideStats.getAmplitudeAverage()));
        assertThat(result.getHigh().getForShipments().getTotal(), is(tidesAboveThreshold));
        assertThat(result.getHigh().getForShipments().getAssigned(), is(tidesAboveThresholdAssignedForShipments));
    }
}
