package com.coopcycle.breton.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommerceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommerceDTO.class);
        CommerceDTO commerceDTO1 = new CommerceDTO();
        commerceDTO1.setId("id1");
        CommerceDTO commerceDTO2 = new CommerceDTO();
        assertThat(commerceDTO1).isNotEqualTo(commerceDTO2);
        commerceDTO2.setId(commerceDTO1.getId());
        assertThat(commerceDTO1).isEqualTo(commerceDTO2);
        commerceDTO2.setId("id2");
        assertThat(commerceDTO1).isNotEqualTo(commerceDTO2);
        commerceDTO1.setId(null);
        assertThat(commerceDTO1).isNotEqualTo(commerceDTO2);
    }
}
