package com.ports.tide.generators;

import com.github.javafaker.Faker;
import com.ports.tide.api.dtos.TidesStatistics;
import com.ports.tide.api.entities.Shipment;
import com.ports.tide.api.entities.Tide;
import com.ports.tide.api.enums.TideType;
import com.ports.tide.api.projections.TideSummaryEntry;
import com.ports.tide.api.projections.TidesGeneralStatisticsEntry;
import com.ports.tide.api.projections.WeeklyReportEntry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Generates entities and DTOs instances for string entries.
 */
public class TidesGenerator {

    /**
     * Fake data generator instance.
     */
    private static final Faker faker = new Faker();

    /**
     * Generates a string entry entity.
     *
     * @param generateId Informs if the generated entity should have an ID.
     * @param numShipments The number of shipments to generate.
     * @return An instance of the entity.
     */
    public static Tide generateTide(boolean generateId, int numShipments) {
        return generateTide(generateId, numShipments, 1L);
    }

    /**
     * Generates a string entry entity with a minimal id.
     *
     * @param generateId Informs if the generated entity should have an ID.
     * @param numShipments The number of shipments to generate.
     * @param minId The minimal id to generate.
     * @return An instance of the entity.
     */
    public static Tide generateTide(boolean generateId, int numShipments, long minId) {
        Tide tide = new Tide();
        tide.setTime(LocalDateTime.ofInstant(
            faker.date().past(1, TimeUnit.DAYS).toInstant(),
            ZoneId.of("Europe/Paris")
        ));
        tide.setAmplitude((float) faker.number().randomDouble(2, 1, 7));

        if (tide.getAmplitude() > 4) {
            tide.setType(TideType.HIGH);
        } else {
            tide.setType(TideType.LOW);
        }

        if (numShipments > 0) {
            tide.setShipments(generateShipments(numShipments, generateId, 1000 + minId));
        }

        if (generateId) {
            tide.setId(minId);
        }

        return tide;
    }

    /**
     * Generates many tides.
     *
     * @param quantity The amount of instances to generate.
     * @param generateId Informs if the generated entities should have IDs.
     * @param numShipments The number of shipments assigned to the tide.
     * @param minId The minimal id to generate.
     * @return A list of entity instances.
     */
    private static List<Tide> generateTides(int quantity, boolean generateId, int numShipments, long minId) {
        List<Tide> tides = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Tide entry = generateTide(false, numShipments, minId);
            if (generateId) {
                entry.setId(i + minId);
            }
            tides.add(entry);
        }

        return tides;
    }

    /**
     * Generates tides with IDs.
     *
     * @param quantity The amount of instances to generate.
     * @param numShipments The number of shipments assigned to the tide.
     * @param minId The minimal id to generate.
     * @return A list of entity instances.
     */
    public static List<Tide> generateTidesWithId(int quantity, int numShipments, long minId) {
        return generateTides(quantity, true, numShipments, minId);
    }

    /**
     * Generates tides without IDs.
     *
     * @param quantity The amount of instances to generate.
     * @param numShipments The number of shipments assigned to the tide.
     * @return A list of entity instances.
     */
    public static List<Tide> generateTidesWithoutId(int quantity, int numShipments) {
        return generateTides(quantity, false, numShipments, 0);
    }

    /**
     * Generates high tides.
     *
     * @param quantity The amount of instances to generate.
     * @param generateId Informs if the generated entities should have IDs.
     * @param numShipments The number of shipments for each tide.
     * @param lowerAmplitudeThreshold The lower threshold for the tide amplitude.
     * @param minId The minimal id to generate.
     * @return A list of high tides.
     */
    public static List<Tide> generateHighTides(
        int quantity, boolean generateId, int numShipments, Float lowerAmplitudeThreshold, long minId
    ) {
        List<Tide> tides = generateTidesFromType(TideType.HIGH, quantity, generateId, numShipments, minId);
        tides.forEach(tide -> {
            if (tide.getAmplitude() <= lowerAmplitudeThreshold) {
                int minValue = (int) Math.ceil(lowerAmplitudeThreshold);
                tide.setAmplitude((float) faker.number()
                    .randomDouble(2, minValue, Math.max(minValue + 1, 7)));
            }
        });

        return tides;
    }

    /**
     * Generates low tides.
     *
     * @param quantity The amount of instances to generate.
     * @param generateId Informs if the generated entities should have IDs.
     * @param minId The minimal id to generate.
     * @return A list of low tides.
     */
    public static List<Tide> generateLowTides(int quantity, boolean generateId, long minId) {
        return generateTidesFromType(TideType.LOW, quantity, generateId, 0, minId);
    }

    /**
     * Generates tides for a specific type.
     *
     * @param type The type of the tides to generate.
     * @param quantity The amount of instances to generate.
     * @param generateId Informs if the generated entities should have IDs.
     * @param numShipments The number of shipments for each tide.
     * @param minId The minimal id to generate.
     * @return A list of low tides.
     */
    private static List<Tide> generateTidesFromType(
        TideType type, int quantity, boolean generateId, int numShipments, long minId
    ) {
        List<Tide> tides = generateTides(quantity, generateId, numShipments, minId);
        tides.forEach(tide -> {
            tide.setType(type);
            if (type.equals(TideType.HIGH)) {
                tide.setAmplitude((float) faker.number().randomDouble(2, 5, 7));
            } else {
                tide.setAmplitude((float) faker.number().randomDouble(2, 1, 3));
            }
        });

        return tides;
    }

    /**
     * Generates a shipment.
     *
     * @param generateId Informs if the generated entity should have an ID.
     * @return A shipment.
     */
    public static Shipment generateShipment(boolean generateId) {
        Shipment shipment = new Shipment();
        shipment.setQuantity(faker.number().numberBetween(100, 5000));

        if (generateId) {
            shipment.setId(1L);
        }

        return shipment;
    }

    /**
     * Generates many shipments.
     *
     * @param quantity The amount of instances to generate.
     * @param generateId Informs if the generated entities should have IDs.
     * @param minId The minimal id to generate.
     * @return A list of entity instances.
     */
    public static List<Shipment> generateShipments(int quantity, boolean generateId, long minId) {
        List<Shipment> shipments = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Shipment entry = TidesGenerator.generateShipment(false);
            if (generateId) {
                entry.setId(i + 1000 + minId);
            }
            shipments.add(entry);
        }

        return shipments;
    }

    public static TideSummaryEntry generateSummaryEntry() {
        return new TideSummaryEntry() {
            @Getter Float amplitude = (float) faker.number().randomDouble(2, 5, 7);
            @Getter LocalDateTime datetime = LocalDateTime.ofInstant(
                faker.date().past(1, TimeUnit.DAYS).toInstant(),
                ZoneId.of("Europe/Paris")
            );
            @Getter Integer numberOfShipments = faker.number().numberBetween(0, 3);
        };
    }

    public static List<TideSummaryEntry> generateSummary(int quantityOfEntries) {
        List<TideSummaryEntry> result = new ArrayList<>();

        for (int i = 0; i < quantityOfEntries; i++) {
            result.add(generateSummaryEntry());
        }

        return result;
    }

    public static WeeklyReportEntry generateWeeklyReportEntry() {
        return new WeeklyReportEntry() {
            @Getter Integer year = faker.number().numberBetween(2010, 2021);
            @Getter Integer week = faker.number().numberBetween(1, 52);
            @Getter Integer numberOfShipments = faker.number().numberBetween(1, 10);
            @Getter Integer totalQuantity = faker.number().numberBetween(100, 10000);
        };
    }

    public static List<WeeklyReportEntry> generateWeeklyReport(int quantityOfEntries) {
        List<WeeklyReportEntry> result = new ArrayList<>();

        for (int i = 0; i < quantityOfEntries; i++) {
            result.add(generateWeeklyReportEntry());
        }

        return result;
    }

    public static TidesGeneralStatisticsEntry generateTidesGeneralStatisticsEntry(TideType type) {
        class TidesGeneralStatisticsEntryWithSetters implements TidesGeneralStatisticsEntry {
            @Getter @Setter TideType tideType = null;
            @Getter @Setter Float amplitudeAverage = null;
        }
        TidesGeneralStatisticsEntryWithSetters result = new TidesGeneralStatisticsEntryWithSetters();

        result.setTideType(type);
        if (type.equals(TideType.HIGH)) {
            result.setAmplitudeAverage((float) faker.number().randomDouble(2, 5, 7));
        } else {
            result.setAmplitudeAverage((float) faker.number().randomDouble(2, 1, 3));
        }

        return result;
    }

    public static TidesStatistics generateTidesStatistics() {
        TidesStatistics result = new TidesStatistics();

        result.getLow().getAmplitude().setAverage(
            (float) faker.number().randomDouble(2, 1, 3)
        );
        result.getHigh().getAmplitude().setAverage(
            (float) faker.number().randomDouble(2, 5, 7)
        );
        result.getHigh().getForShipments().setTotal(
            faker.number().numberBetween(10, 20)
        );
        result.getHigh().getForShipments().setAssigned(
            faker.number().numberBetween(1, 10)
        );

        return result;
    }
}
