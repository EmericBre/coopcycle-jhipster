package com.coopcycle.breton.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivreurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livreur.class);
        Livreur livreur1 = new Livreur();
        livreur1.setId("id1");
        Livreur livreur2 = new Livreur();
        livreur2.setId(livreur1.getId());
        assertThat(livreur1).isEqualTo(livreur2);
        livreur2.setId("id2");
        assertThat(livreur1).isNotEqualTo(livreur2);
        livreur1.setId(null);
        assertThat(livreur1).isNotEqualTo(livreur2);
    }
}
