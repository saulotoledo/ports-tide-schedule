package com.ports.tide.core.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Mix-in interface used to customize pages serialization.
 *
 * @param <T> The page elements type.
 */
public interface PageMixIn<T> {

    /**
     * {@inheritDoc}
     */
    @JsonProperty("items")
    List<T> getContent();
}
