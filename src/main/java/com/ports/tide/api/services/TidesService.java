package com.ports.tide.api.services;

import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.enums.TideType;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.TidesGeneralStatisticsEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import com.ports.tide.api.repositories.TidesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A service for operations with tides.
 */
@Service
@RequiredArgsConstructor
public class TidesService {

    @Value("${tides.high.shipments.threshold}")
    private Float highTidesShipmentsThreshold;

    /**
     * The tides repository.
     */
    private final TidesRepository repository;

    /**
     * Returns the tides summary.
     *
     * @param pageable Object containing pagination information.
     * @return A list of tides formatted as a summary.
     */
    public Page<TideSummaryEntry> getSummary(Pageable pageable) {
        return this.repository.findSummaryTides(pageable);
    }

    /**
     * Returns the tides summary.
     *
     * @param pageable Object containing pagination information.
     * @return A list of tides formatted as a summary.
     */
    public Page<WeeklyReportEntry> getWeeklyReport(Pageable pageable) {
        return this.repository.findWeeklyReport(pageable);
    }

    /**
     * Computes and returns tides statistics.
     *
     * @return The computed statistics.
     */
    public TidesStatistics getStatistics() {
        List<TidesGeneralStatisticsEntry> tidesGeneralStatistics = this.repository.computeGeneralStatistics();

        Optional<TidesGeneralStatisticsEntry> lowTidesStats = tidesGeneralStatistics.stream()
            .filter(stats -> TideType.LOW.equals(stats.getTideType()))
            .findFirst();

        Optional<TidesGeneralStatisticsEntry> highTidesStats = tidesGeneralStatistics.stream()
            .filter(stats -> TideType.HIGH.equals(stats.getTideType()))
            .findFirst();

        TidesStatistics ts = new TidesStatistics();
        if (lowTidesStats.isPresent()) {
            ts.getLow().getAmplitude().setAverage(lowTidesStats.get().getAmplitudeAverage());
        }
        if (highTidesStats.isPresent()) {
            ts.getHigh().getAmplitude().setAverage(highTidesStats.get().getAmplitudeAverage());
        }

        ts.getHigh().getForShipments().setTotal(
            this.repository.countByTideTypeWithAmplitudeGreaterThan(TideType.HIGH, this.highTidesShipmentsThreshold)
        );
        ts.getHigh().getForShipments().setAssigned(
            this.repository.countAssignedShipmentsByTideTypeWithAmplitudeGreaterThan(
                TideType.HIGH, this.highTidesShipmentsThreshold
            )
        );

        return ts;
    }
}
