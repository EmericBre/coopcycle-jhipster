package com.coopcycle.breton.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.breton.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisateurDTO.class);
        UtilisateurDTO utilisateurDTO1 = new UtilisateurDTO();
        utilisateurDTO1.setId("id1");
        UtilisateurDTO utilisateurDTO2 = new UtilisateurDTO();
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
        utilisateurDTO2.setId(utilisateurDTO1.getId());
        assertThat(utilisateurDTO1).isEqualTo(utilisateurDTO2);
        utilisateurDTO2.setId("id2");
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
        utilisateurDTO1.setId(null);
        assertThat(utilisateurDTO1).isNotEqualTo(utilisateurDTO2);
    }
}
