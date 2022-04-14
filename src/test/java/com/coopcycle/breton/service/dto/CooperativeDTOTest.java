package com.coopcycle.breton.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeDTO.class);
        CooperativeDTO cooperativeDTO1 = new CooperativeDTO();
        cooperativeDTO1.setId("id1");
        CooperativeDTO cooperativeDTO2 = new CooperativeDTO();
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
        cooperativeDTO2.setId(cooperativeDTO1.getId());
        assertThat(cooperativeDTO1).isEqualTo(cooperativeDTO2);
        cooperativeDTO2.setId("id2");
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
        cooperativeDTO1.setId(null);
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
    }
}
