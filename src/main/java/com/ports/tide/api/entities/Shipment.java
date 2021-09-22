package com.ports.tide.api.entities;

import com.ports.tide.core.entities.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Describes a shipment.
 */
@Entity
@Table(name = "shipments")
@Getter
@Setter
public class Shipment extends IdentifiableEntity {

    /**
     * The quantity of items in this shipment.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * The order items.
     */
    @ManyToOne()
    @JoinColumn()
    private Tide tide;
}
