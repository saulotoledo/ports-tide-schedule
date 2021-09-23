package com.ports.tide.api.repositories;

import com.ports.tide.api.entities.Tide;
import com.ports.tide.api.enums.TideType;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.TidesGeneralStatisticsEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    @Query(
        value = "SELECT EXTRACT(YEAR FROM t.time) AS year, EXTRACT(WEEK FROM t.time) AS week, "
            + "COUNT(s.id) AS numberOfShipments, SUM(s.quantity) AS totalQuantity FROM tides t "
            + "LEFT JOIN shipments s ON s.tide_id = t.id GROUP BY 1, 2",
        countQuery = "SELECT COUNT(DISTINCT EXTRACT(YEAR FROM t.time), EXTRACT(WEEK FROM t.time)) FROM tides t",
        nativeQuery = true
    )
    Page<WeeklyReportEntry> findWeeklyReport(Pageable pageable);

    /**
     * Returns general statistics for each tide type.
     *
     * @return General statistics for each tide type.
     */
    @Query(value = "SELECT t.type AS tideType, AVG(t.amplitude) AS amplitudeAverage FROM Tide t GROUP BY t.type")
    List<TidesGeneralStatisticsEntry> computeGeneralStatistics();

    /**
     * Returns the number of tides of a given type and above the informed threshold.
     *
     * @param type The type of the tides to filter.
     * @param threshold The threshold to use.
     * @return The number of tides according to the informed details.
     */
    @Query(value = "SELECT COUNT(DISTINCT t.id) FROM Tide t WHERE t.type = ?1 AND t.amplitude > ?2")
    Integer countByTideTypeWithAmplitudeGreaterThan(TideType type, Float threshold);

    /**
     * Returns the number of tides of a given type and above the informed threshold assigned for shipments.
     *
     * @param type The type of the tides to filter.
     * @param threshold The threshold to use.
     * @return The number of tides according to the informed details.
     */
    @Query(value = "SELECT COUNT(DISTINCT s.tide) FROM Tide t LEFT JOIN t.shipments s "
        + "WHERE t.type = ?1 AND t.amplitude > ?2")
    Integer countAssignedShipmentsByTideTypeWithAmplitudeGreaterThan(TideType type, Float threshold);
}
