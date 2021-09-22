package com.ports.tide.api.repositories;

import com.ports.tide.api.entities.Tide;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for string entries.
 */
@Repository
public interface TidesRepository extends JpaRepository<Tide, Long> {

    /**
     * Returns a summary of tides containing the amplitude, the date and time, and the number of shipments.
     *
     * @param pageable Object containing pagination information.
     * @return The requested summary.
     */
    @Query(value = "SELECT t.amplitude AS amplitude, t.time AS datetime, COUNT(s.id) AS numberOfShipments "
        + "FROM Tide t LEFT JOIN t.shipments s GROUP BY t.id")
    Page<TideSummaryEntry> findSummaryTides(Pageable pageable);

    /**
     * Returns a weekly report for tides. The underlining database MUST support the EXTRACT() function.
     *
     * @param pageable Object containing pagination information.
     * @return A list of values matching the informed string.
     */
    @Query(value = "SELECT EXTRACT(YEAR FROM t.time) AS year, EXTRACT(WEEK FROM t.time) AS week, "
        + "COUNT(s.id) AS numberOfShipments, SUM(s.quantity) AS totalQuantity FROM Tide t "
        + "LEFT JOIN t.shipments s GROUP BY week, year")
    Page<WeeklyReportEntry> findWeeklyReport(Pageable pageable);
}
