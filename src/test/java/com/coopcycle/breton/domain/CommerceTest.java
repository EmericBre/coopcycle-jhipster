package com.coopcycle.breton.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommerceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commerce.class);
        Commerce commerce1 = new Commerce();
        commerce1.setId("id1");
        Commerce commerce2 = new Commerce();
        commerce2.setId(commerce1.getId());
        assertThat(commerce1).isEqualTo(commerce2);
        commerce2.setId("id2");
        assertThat(commerce1).isNotEqualTo(commerce2);
        commerce1.setId(null);
        assertThat(commerce1).isNotEqualTo(commerce2);
    }
}
