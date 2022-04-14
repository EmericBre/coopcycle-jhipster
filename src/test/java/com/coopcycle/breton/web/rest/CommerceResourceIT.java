package com.coopcycle.breton.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.coopcycle.breton.IntegrationTest;
import com.coopcycle.breton.domain.Commerce;
import com.coopcycle.breton.repository.CommerceRepository;
import com.coopcycle.breton.repository.EntityManager;
import com.coopcycle.breton.service.CommerceService;
import com.coopcycle.breton.service.dto.CommerceDTO;
import com.coopcycle.breton.service.mapper.CommerceMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link CommerceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommerceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commerce";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CommerceRepository commerceRepository;

    @Mock
    private CommerceRepository commerceRepositoryMock;

    @Autowired
    private CommerceMapper commerceMapper;

    @Mock
    private CommerceService commerceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Commerce commerce;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commerce createEntity(EntityManager em) {
        Commerce commerce = new Commerce().name(DEFAULT_NAME).adress(DEFAULT_ADRESS);
        return commerce;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commerce createUpdatedEntity(EntityManager em) {
        Commerce commerce = new Commerce().name(UPDATED_NAME).adress(UPDATED_ADRESS);
        return commerce;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_commerce__cooperative").block();
            em.deleteAll(Commerce.class).block();
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
        commerce = createEntity(em);
    }

    @Test
    void createCommerce() throws Exception {
        int databaseSizeBeforeCreate = commerceRepository.findAll().collectList().block().size();
        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeCreate + 1);
        Commerce testCommerce = commerceList.get(commerceList.size() - 1);
        assertThat(testCommerce.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommerce.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    void createCommerceWithExistingId() throws Exception {
        // Create the Commerce with an existing ID
        commerce.setId("existing_id");
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        int databaseSizeBeforeCreate = commerceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commerceRepository.findAll().collectList().block().size();
        // set the field null
        commerce.setName(null);

        // Create the Commerce, which fails.
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = commerceRepository.findAll().collectList().block().size();
        // set the field null
        commerce.setAdress(null);

        // Create the Commerce, which fails.
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCommerceAsStream() {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        List<Commerce> commerceList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CommerceDTO.class)
            .getResponseBody()
            .map(commerceMapper::toEntity)
            .filter(commerce::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(commerceList).isNotNull();
        assertThat(commerceList).hasSize(1);
        Commerce testCommerce = commerceList.get(0);
        assertThat(testCommerce.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommerce.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    void getAllCommerce() {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        // Get all the commerceList
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
            .value(hasItem(commerce.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].adress")
            .value(hasItem(DEFAULT_ADRESS));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommerceWithEagerRelationshipsIsEnabled() {
        when(commerceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(commerceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommerceWithEagerRelationshipsIsNotEnabled() {
        when(commerceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(commerceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCommerce() {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        // Get the commerce
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commerce.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commerce.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.adress")
            .value(is(DEFAULT_ADRESS));
    }

    @Test
    void getNonExistingCommerce() {
        // Get the commerce
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCommerce() throws Exception {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();

        // Update the commerce
        Commerce updatedCommerce = commerceRepository.findById(commerce.getId()).block();
        updatedCommerce.name(UPDATED_NAME).adress(UPDATED_ADRESS);
        CommerceDTO commerceDTO = commerceMapper.toDto(updatedCommerce);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commerceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
        Commerce testCommerce = commerceList.get(commerceList.size() - 1);
        assertThat(testCommerce.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommerce.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    void putNonExistingCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commerceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommerceWithPatch() throws Exception {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();

        // Update the commerce using partial update
        Commerce partialUpdatedCommerce = new Commerce();
        partialUpdatedCommerce.setId(commerce.getId());

        partialUpdatedCommerce.adress(UPDATED_ADRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommerce.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommerce))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
        Commerce testCommerce = commerceList.get(commerceList.size() - 1);
        assertThat(testCommerce.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommerce.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    void fullUpdateCommerceWithPatch() throws Exception {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();

        // Update the commerce using partial update
        Commerce partialUpdatedCommerce = new Commerce();
        partialUpdatedCommerce.setId(commerce.getId());

        partialUpdatedCommerce.name(UPDATED_NAME).adress(UPDATED_ADRESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommerce.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommerce))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
        Commerce testCommerce = commerceList.get(commerceList.size() - 1);
        assertThat(testCommerce.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommerce.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    void patchNonExistingCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commerceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommerce() throws Exception {
        int databaseSizeBeforeUpdate = commerceRepository.findAll().collectList().block().size();
        commerce.setId(UUID.randomUUID().toString());

        // Create the Commerce
        CommerceDTO commerceDTO = commerceMapper.toDto(commerce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commerceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commerce in the database
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommerce() {
        // Initialize the database
        commerce.setId(UUID.randomUUID().toString());
        commerceRepository.save(commerce).block();

        int databaseSizeBeforeDelete = commerceRepository.findAll().collectList().block().size();

        // Delete the commerce
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commerce.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Commerce> commerceList = commerceRepository.findAll().collectList().block();
        assertThat(commerceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
