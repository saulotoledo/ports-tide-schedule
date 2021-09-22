package com.ports.tide.api.services;

import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.repositories.TidesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * A service for operations with tides.
 */
@Service
@RequiredArgsConstructor
public class TidesService {

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
}
