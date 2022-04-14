package com.coopcycle.breton.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommerceMapperTest {

    private CommerceMapper commerceMapper;

    @BeforeEach
    public void setUp() {
        commerceMapper = new CommerceMapperImpl();
    }
}
