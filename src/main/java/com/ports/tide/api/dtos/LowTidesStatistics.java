package com.ports.tide.api.dtos;

import lombok.Getter;

/**
 * DTO for representing low tide statistics.
 */
public class LowTidesStatistics {

    /**
     * Amplitude statistics.
     */
    @Getter
    private AmplitudeStatistics amplitude = new AmplitudeStatistics();
}
