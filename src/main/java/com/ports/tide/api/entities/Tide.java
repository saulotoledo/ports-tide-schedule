package com.ports.tide.api.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ports.tide.api.enums.TideType;
import com.ports.tide.core.entities.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a tide.
 */
@Entity
@Table(name = "tides")
@Getter
@Setter
public class Tide extends IdentifiableEntity {

    /**
     * The type of the tide.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private TideType type;

    /**
     * The time of the tide.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Column(nullable = false)
    private LocalDateTime time;

    /**
     * The amplitude of the tide.
     */
    @Column(nullable = false)
    private Float amplitude;

    /**
     * The shipments this tide is assigned for.
     */
    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST }, mappedBy = "tide")
    private List<Shipment> shipments = new ArrayList<>();
}
