package com.ports.tide.api.projections;

/**
 * Represents a weekly tides report entry.
 */
public interface WeeklyReportEntry {

    /**
     * Returns the year of the entry.
     *
     * @return The year of the entry.
     */
    Integer getYear();

    /**
     * Returns the week of the year for the entry.
     *
     * @return The week of the year for the entry.
     */
    Integer getWeek();

    /**
     * Returns the number of shipments for the week.
     *
     * @return The number of shipments for the week.
     */
    Integer getNumberOfShipments();

    /**
     * Returns the total quantity for shipments for the week.
     *
     * @return The total quantity for shipments for the week.
     */
    Integer getTotalQuantity();
}
