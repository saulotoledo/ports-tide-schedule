package com.ports.tide.api.controllers;

import com.ports.tide.api.projections.TideSummaryEntry;
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
}
