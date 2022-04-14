package com.coopcycle.breton.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.coopcycle.breton.IntegrationTest;
import com.coopcycle.breton.domain.Livreur;
import com.coopcycle.breton.repository.EntityManager;
import com.coopcycle.breton.repository.LivreurRepository;
import com.coopcycle.breton.service.dto.LivreurDTO;
import com.coopcycle.breton.service.mapper.LivreurMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link LivreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LivreurResourceIT {

    private static final String DEFAULT_FIRSTNAME = "Rbcz";
    private static final String UPDATED_FIRSTNAME = "Lrvvvqu";

    private static final String DEFAULT_LASTNAME = "Nbzj";
    private static final String UPDATED_LASTNAME = "Wvw";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private LivreurMapper livreurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Livreur livreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createEntity(EntityManager em) {
        Livreur livreur = new Livreur().firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME).phone(DEFAULT_PHONE);
        return livreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createUpdatedEntity(EntityManager em) {
        Livreur livreur = new Livreur().firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE);
        return livreur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Livreur.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        livreur = createEntity(em);
    }

    @Test
    void createLivreur() throws Exception {
        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();
        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate + 1);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testLivreur.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testLivreur.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void createLivreurWithExistingId() throws Exception {
        // Create the Livreur with an existing ID
        livreur.setId("existing_id");
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setFirstname(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setLastname(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setPhone(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLivreursAsStream() {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        List<Livreur> livreurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LivreurDTO.class)
            .getResponseBody()
            .map(livreurMapper::toEntity)
            .filter(livreur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(livreurList).isNotNull();
        assertThat(livreurList).hasSize(1);
        Livreur testLivreur = livreurList.get(0);
        assertThat(testLivreur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testLivreur.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testLivreur.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void getAllLivreurs() {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        // Get all the livreurList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(livreur.getId()))
            .jsonPath("$.[*].firstname")
            .value(hasItem(DEFAULT_FIRSTNAME))
            .jsonPath("$.[*].lastname")
            .value(hasItem(DEFAULT_LASTNAME))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE));
    }

    @Test
    void getLivreur() {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(livreur.getId()))
            .jsonPath("$.firstname")
            .value(is(DEFAULT_FIRSTNAME))
            .jsonPath("$.lastname")
            .value(is(DEFAULT_LASTNAME))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE));
    }

    @Test
    void getNonExistingLivreur() {
        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewLivreur() throws Exception {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).block();
        updatedLivreur.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE);
        LivreurDTO livreurDTO = livreurMapper.toDto(updatedLivreur);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testLivreur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testLivreur.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void putNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testLivreur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testLivreur.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void fullUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).phone(UPDATED_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testLivreur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testLivreur.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void patchNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(UUID.randomUUID().toString());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLivreur() {
        // Initialize the database
        livreur.setId(UUID.randomUUID().toString());
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeDelete = livreurRepository.findAll().collectList().block().size();

        // Delete the livreur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
