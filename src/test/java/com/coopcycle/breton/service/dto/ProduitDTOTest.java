package com.coopcycle.breton.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProduitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProduitDTO.class);
        ProduitDTO produitDTO1 = new ProduitDTO();
        produitDTO1.setId("id1");
        ProduitDTO produitDTO2 = new ProduitDTO();
        assertThat(produitDTO1).isNotEqualTo(produitDTO2);
        produitDTO2.setId(produitDTO1.getId());
        assertThat(produitDTO1).isEqualTo(produitDTO2);
        produitDTO2.setId("id2");
        assertThat(produitDTO1).isNotEqualTo(produitDTO2);
        produitDTO1.setId(null);
        assertThat(produitDTO1).isNotEqualTo(produitDTO2);
    }
}
