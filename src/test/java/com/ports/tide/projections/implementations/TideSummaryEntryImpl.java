package com.ports.tide.projections.implementations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ports.tide.api.projections.TideSummaryEntry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TideSummaryEntryImpl implements TideSummaryEntry {
    @Getter @Setter Float amplitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Getter @Setter LocalDateTime datetime;

    @Getter @Setter Integer numberOfShipments;
}
