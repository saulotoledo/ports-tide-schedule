package com.ports.tide.api.projections;

import com.ports.tide.api.enums.TideType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Database projection interface for tides general statistics.
 */
public interface TidesGeneralStatisticsEntry {

    /**
     * Returns the tide type.
     *
     * @return The tide type.
     */
    @Enumerated(EnumType.STRING)
    TideType getTideType();

    /**
     * Returns the amplitude average for this entry.
     *
     * @return The amplitude average for this entry.
     */
    Float getAmplitudeAverage();
}
