package com.ports.tide.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ports.tide.api.entities.Tide;
import com.ports.tide.api.repositories.TidesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Initializes the database if empty.
 */
@Configuration
public class InitDbConfiguration {

    /**
     * Repository for tides.
     */
    @Autowired
    private TidesRepository tidesRepository;

    /**
     * Springboot object mapper instance.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Initializes the database from the tides.json file if the database is empty.
     *
     * @throws IOException If the tides.json file is missing.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initDatabaseOnStartup() throws IOException {
        if (this.tidesRepository.count() == 0) {
            ArrayNode jsonTides = (ArrayNode) this.objectMapper
                .readTree(new ClassPathResource("data/tides.json").getFile())
                .get("items");

            jsonTides.forEach(jsonTide -> {
                Tide tide = this.objectMapper.convertValue(jsonTide, Tide.class);
                tide.getShipments().forEach(shipment -> shipment.setTide(tide));
                this.tidesRepository.save(tide);
            });
        }
    }
}
