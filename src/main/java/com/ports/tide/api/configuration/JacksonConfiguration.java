package com.ports.tide.api.configuration;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Slice;

import com.ports.tide.core.mixins.PageMixIn;

/**
 * Jackson configuration.
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Customizes the Jackson object mapper.
     *
     * @return A customized version of the Jackson object mapper.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.mixIn(Slice.class, PageMixIn.class);
    }
}
