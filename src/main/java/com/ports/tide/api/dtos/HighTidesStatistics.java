package com.ports.tide.api.dtos;

import lombok.Getter;

/**
 * DTO for representing high tide statistics.
 */
public class HighTidesStatistics {

    /**
     * Amplitude statistics.
     */
    @Getter
    private AmplitudeStatistics amplitude = new AmplitudeStatistics();

    /**
     * Statistics for shipments.
     */
    @Getter
    private ForShipmentsStatistics forShipments = new ForShipmentsStatistics();
}
