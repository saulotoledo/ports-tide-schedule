package com.ports.tide.api.repositories;

import com.ports.tide.api.entities.Tide;
import com.ports.tide.api.projections.TideSummaryEntry;
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
}
