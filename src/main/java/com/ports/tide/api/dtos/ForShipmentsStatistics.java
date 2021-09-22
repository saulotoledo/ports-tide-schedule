package com.ports.tide.api.dtos;

import lombok.Data;

/**
 * DTO for representing amplitude statistics.
 */
public @Data class ForShipmentsStatistics {

    /**
     * The total of tides in which shipments can arrive.
     */
    private Integer total;

    /**
     * The total of tides assigned for shipments.
     */
    private Integer assigned;
}
