package com.ports.tide.projections.implementations;

import com.ports.tide.api.projections.WeeklyReportEntry;
import lombok.Getter;
import lombok.Setter;

public class WeeklyReportEntryImpl implements WeeklyReportEntry {
    @Getter @Setter Integer year;
    @Getter @Setter Integer week;
    @Getter @Setter Integer numberOfShipments;
    @Getter @Setter Integer totalQuantity;
}
