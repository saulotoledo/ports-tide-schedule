package com.ports.tide.api.repositories;

import com.ports.tide.api.entities.Tide;
import com.ports.tide.generators.TidesGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@DisplayName("Integration tests for the tide repository with the database layer")
public class TidesRepositoryJpaTest {

    @Autowired
    private TidesRepository tidesRepository;

    private final List<Tide> defaultEntriesWithShipments = TidesGenerator.generateTidesWithoutId(5, 2);
    private final List<Tide> defaultEntriesWithoutShipments = TidesGenerator.generateTidesWithoutId(5, 0);

    @BeforeEach
    public void setUp() {
        this.defaultEntriesWithShipments.forEach(entry -> this.tidesRepository.save(entry));
        this.defaultEntriesWithoutShipments.forEach(entry -> this.tidesRepository.save(entry));
    }

    @Test
    @DisplayName("Should return a specific item from the database")
    public void testFindOneExistingItem() {
        List<Tide> allItems = this.tidesRepository.findAll();
        Tide itemToCompare = allItems.get(2);
        Long idToFind = itemToCompare.getId();

        @SuppressWarnings("OptionalGetWithoutIsPresent") Tide singleItem = this.tidesRepository.findById(idToFind).get();
        assertThat(singleItem, is(not(nullValue())));
        assertThat(singleItem.getAmplitude(), is(itemToCompare.getAmplitude()));
        assertThat(singleItem.getShipments(), is(itemToCompare.getShipments()));
        assertThat(singleItem.getTime(), is(itemToCompare.getTime()));
        assertThat(singleItem.getType(), is(itemToCompare.getType()));
    }

    @Test
    @DisplayName("Should return an empty optional if an item does not exist in the database")
    public void testFindOneNonExistingItem() {
        Optional<Tide> singleItem = this.tidesRepository.findById(10000L);
        assertThat(singleItem.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Should return the second page for a request with the correct elements")
    public void testReturnTheSecondPageWithoutInformingFilter() {
        // By default, Spring starts counting the page from 0. Thus, page 1 is the second page
        int page = 1;
        int size = 2;

        // The default entries with shipments are the first items saved to the database
        List<Tide> elementsToReturn = this.defaultEntriesWithShipments.subList(page * size, page * size + size);

        Page<Tide> result = this.tidesRepository.findAll(
            PageRequest.of(page, size)
        );

        assertThat(result.getNumberOfElements(), is(size));
        assertThat(result.getTotalElements(), is(
            (long) this.defaultEntriesWithShipments.size() + this.defaultEntriesWithoutShipments.size()
        ));
        assertThat(result, contains(elementsToReturn.get(0), elementsToReturn.get(1)));
    }

    // TODO: Add tests for our custom queries
}
