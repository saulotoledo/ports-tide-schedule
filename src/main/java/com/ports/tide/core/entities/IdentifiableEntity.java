package com.ports.tide.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Generic identifiable entity.
 */
@MappedSuperclass
public abstract class IdentifiableEntity {

    /**
     * The (auto-generated) ID of the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter Long id;
}
