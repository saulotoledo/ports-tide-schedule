package com.ports.tide.api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Tide type.
 */
public enum TideType {
    @JsonProperty("low")
    LOW("low"),

    @JsonProperty("high")
    HIGH("high");

    private final String value;

    TideType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
