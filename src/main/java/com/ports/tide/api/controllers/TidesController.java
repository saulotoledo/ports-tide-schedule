package com.ports.tide.api.controllers;

import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import com.ports.tide.api.services.TidesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for operations with tides.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tides")
public class TidesController {

    /**
     * The tides service.
     */
    private final TidesService tidesService;

    /**
     * Returns a flat summary of tides.
     *
     * @param pageable Pageable object build by Spring to control page, size and sort attributes.
     * @return A flat summary of tides.
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TideSummaryEntry> getSummary(Pageable pageable) {
        return this.tidesService.getSummary(pageable);
    }

    /**
     * Returns weekly report for tides.
     *
     * @param pageable Pageable object build by Spring to control page, size and sort attributes.
     * @return A weekly report of tides.
     */
    @GetMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<WeeklyReportEntry> getReport(Pageable pageable) {
        return this.tidesService.getWeeklyReport(pageable);
    }

    /**
     * Returns statistics for each tide type.
     *
     * @return Statistics for each tide type.
     */
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public TidesStatistics getStatistics() {
        return this.tidesService.getStatistics();
    }
}
