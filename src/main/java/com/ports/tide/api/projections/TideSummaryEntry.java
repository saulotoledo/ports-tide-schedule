package com.ports.tide.api.projections;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Database projection interface for a tide summary entry.
 * A tide summary entry contains a flat and summarized representation of a tide.
 */
public interface TideSummaryEntry {

    /**
     * Returns the amplitude of the tide.
     *
     * @return The amplitude of the tide.
     */
    Float getAmplitude();

    /**
     * Returns the date and the time of the tide.
     *
     * @return The date and the time of the tide.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    LocalDateTime getDatetime();

    /**
     * Returns the number of shipments in the tide.
     *
     * @return The number of shipments in the tide.
     */
    Integer getNumberOfShipments();
}
