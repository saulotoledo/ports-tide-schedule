package com.ports.tide.api.dtos;

import lombok.Getter;

/**
 * DTO for representing tide statistics.
 */
public class TidesStatistics {

    /**
     * Statistics for low tides.
     */
    @Getter
    private LowTidesStatistics low = new LowTidesStatistics();

    /**
     * Statistics for high tides.
     */
    @Getter
    private HighTidesStatistics high = new HighTidesStatistics();
}
