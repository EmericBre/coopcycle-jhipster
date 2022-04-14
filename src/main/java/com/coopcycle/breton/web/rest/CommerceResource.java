package com.coopcycle.breton.web.rest;

import com.coopcycle.breton.repository.CommerceRepository;
import com.coopcycle.breton.service.CommerceService;
import com.coopcycle.breton.service.dto.CommerceDTO;
import com.coopcycle.breton.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.coopcycle.breton.domain.Commerce}.
 */
@RestController
@RequestMapping("/api")
public class CommerceResource {

    private final Logger log = LoggerFactory.getLogger(CommerceResource.class);

    private static final String ENTITY_NAME = "commerce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommerceService commerceService;

    private final CommerceRepository commerceRepository;

    public CommerceResource(CommerceService commerceService, CommerceRepository commerceRepository) {
        this.commerceService = commerceService;
        this.commerceRepository = commerceRepository;
    }

    /**
     * {@code POST  /commerce} : Create a new commerce.
     *
     * @param commerceDTO the commerceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commerceDTO, or with status {@code 400 (Bad Request)} if the commerce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commerce")
    public Mono<ResponseEntity<CommerceDTO>> createCommerce(@Valid @RequestBody CommerceDTO commerceDTO) throws URISyntaxException {
        log.debug("REST request to save Commerce : {}", commerceDTO);
        if (commerceDTO.getId() != null) {
            throw new BadRequestAlertException("A new commerce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return commerceService
            .save(commerceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/commerce/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /commerce/:id} : Updates an existing commerce.
     *
     * @param id the id of the commerceDTO to save.
     * @param commerceDTO the commerceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commerceDTO,
     * or with status {@code 400 (Bad Request)} if the commerceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commerceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commerce/{id}")
    public Mono<ResponseEntity<CommerceDTO>> updateCommerce(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CommerceDTO commerceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Commerce : {}, {}", id, commerceDTO);
        if (commerceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commerceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commerceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return commerceService
                    .update(commerceDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /commerce/:id} : Partial updates given fields of an existing commerce, field will ignore if it is null
     *
     * @param id the id of the commerceDTO to save.
     * @param commerceDTO the commerceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commerceDTO,
     * or with status {@code 400 (Bad Request)} if the commerceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commerceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commerceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commerce/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CommerceDTO>> partialUpdateCommerce(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CommerceDTO commerceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commerce partially : {}, {}", id, commerceDTO);
        if (commerceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commerceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commerceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CommerceDTO> result = commerceService.partialUpdate(commerceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /commerce} : get all the commerce.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commerce in body.
     */
    @GetMapping("/commerce")
    public Mono<List<CommerceDTO>> getAllCommerce(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Commerce");
        return commerceService.findAll().collectList();
    }

    /**
     * {@code GET  /commerce} : get all the commerce as a stream.
     * @return the {@link Flux} of commerce.
     */
    @GetMapping(value = "/commerce", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CommerceDTO> getAllCommerceAsStream() {
        log.debug("REST request to get all Commerce as a stream");
        return commerceService.findAll();
    }

    /**
     * {@code GET  /commerce/:id} : get the "id" commerce.
     *
     * @param id the id of the commerceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commerceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commerce/{id}")
    public Mono<ResponseEntity<CommerceDTO>> getCommerce(@PathVariable String id) {
        log.debug("REST request to get Commerce : {}", id);
        Mono<CommerceDTO> commerceDTO = commerceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commerceDTO);
    }

    /**
     * {@code DELETE  /commerce/:id} : delete the "id" commerce.
     *
     * @param id the id of the commerceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commerce/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCommerce(@PathVariable String id) {
        log.debug("REST request to delete Commerce : {}", id);
        return commerceService
            .delete(id)
            .map(result ->
                ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
            );
    }
}
