package com.coopcycle.breton.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.coopcycle.breton.IntegrationTest;
import com.coopcycle.breton.domain.Utilisateur;
import com.coopcycle.breton.repository.EntityManager;
import com.coopcycle.breton.repository.UtilisateurRepository;
import com.coopcycle.breton.service.dto.UtilisateurDTO;
import com.coopcycle.breton.service.mapper.UtilisateurMapper;
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
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_FIRSTNAME = "Zd";
    private static final String UPDATED_FIRSTNAME = "Zho";

    private static final String DEFAULT_LASTNAME = "Yr";
    private static final String UPDATED_LASTNAME = "Vfbbzlw";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Utilisateur utilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .mail(DEFAULT_MAIL)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS);
        return utilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity(EntityManager em) {
        Utilisateur utilisateur = new Utilisateur()
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .mail(UPDATED_MAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);
        return utilisateur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Utilisateur.class).block();
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
        utilisateur = createEntity(em);
    }

    @Test
    void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().collectList().block().size();
        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUtilisateur.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUtilisateur.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testUtilisateur.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUtilisateur.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId("existing_id");
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        int databaseSizeBeforeCreate = utilisateurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurRepository.findAll().collectList().block().size();
        // set the field null
        utilisateur.setFirstname(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurRepository.findAll().collectList().block().size();
        // set the field null
        utilisateur.setLastname(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilisateurRepository.findAll().collectList().block().size();
        // set the field null
        utilisateur.setAddress(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUtilisateursAsStream() {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        List<Utilisateur> utilisateurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UtilisateurDTO.class)
            .getResponseBody()
            .map(utilisateurMapper::toEntity)
            .filter(utilisateur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(utilisateurList).isNotNull();
        assertThat(utilisateurList).hasSize(1);
        Utilisateur testUtilisateur = utilisateurList.get(0);
        assertThat(testUtilisateur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUtilisateur.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUtilisateur.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testUtilisateur.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUtilisateur.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    void getAllUtilisateurs() {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        // Get all the utilisateurList
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
            .value(hasItem(utilisateur.getId()))
            .jsonPath("$.[*].firstname")
            .value(hasItem(DEFAULT_FIRSTNAME))
            .jsonPath("$.[*].lastname")
            .value(hasItem(DEFAULT_LASTNAME))
            .jsonPath("$.[*].mail")
            .value(hasItem(DEFAULT_MAIL))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS));
    }

    @Test
    void getUtilisateur() {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        // Get the utilisateur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(utilisateur.getId()))
            .jsonPath("$.firstname")
            .value(is(DEFAULT_FIRSTNAME))
            .jsonPath("$.lastname")
            .value(is(DEFAULT_LASTNAME))
            .jsonPath("$.mail")
            .value(is(DEFAULT_MAIL))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS));
    }

    @Test
    void getNonExistingUtilisateur() {
        // Get the utilisateur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewUtilisateur() throws Exception {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).block();
        updatedUtilisateur
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .mail(UPDATED_MAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(updatedUtilisateur);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, utilisateurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUtilisateur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUtilisateur.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    void putNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, utilisateurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.lastname(UPDATED_LASTNAME).mail(UPDATED_MAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUtilisateur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUtilisateur.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .mail(UPDATED_MAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisateur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurList.get(utilisateurList.size() - 1);
        assertThat(testUtilisateur.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUtilisateur.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUtilisateur.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testUtilisateur.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUtilisateur.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    void patchNonExistingUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, utilisateurDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().collectList().block().size();
        utilisateur.setId(UUID.randomUUID().toString());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(utilisateurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUtilisateur() {
        // Initialize the database
        utilisateur.setId(UUID.randomUUID().toString());
        utilisateurRepository.save(utilisateur).block();

        int databaseSizeBeforeDelete = utilisateurRepository.findAll().collectList().block().size();

        // Delete the utilisateur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, utilisateur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Utilisateur> utilisateurList = utilisateurRepository.findAll().collectList().block();
        assertThat(utilisateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
