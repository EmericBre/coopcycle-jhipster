package com.coopcycle.breton.service;

import com.coopcycle.breton.domain.Commerce;
import com.coopcycle.breton.repository.CommerceRepository;
import com.coopcycle.breton.service.dto.CommerceDTO;
import com.coopcycle.breton.service.mapper.CommerceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Commerce}.
 */
@Service
@Transactional
public class CommerceService {

    private final Logger log = LoggerFactory.getLogger(CommerceService.class);

    private final CommerceRepository commerceRepository;

    private final CommerceMapper commerceMapper;

    public CommerceService(CommerceRepository commerceRepository, CommerceMapper commerceMapper) {
        this.commerceRepository = commerceRepository;
        this.commerceMapper = commerceMapper;
    }

    /**
     * Save a commerce.
     *
     * @param commerceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommerceDTO> save(CommerceDTO commerceDTO) {
        log.debug("Request to save Commerce : {}", commerceDTO);
        return commerceRepository.save(commerceMapper.toEntity(commerceDTO)).map(commerceMapper::toDto);
    }

    /**
     * Update a commerce.
     *
     * @param commerceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommerceDTO> update(CommerceDTO commerceDTO) {
        log.debug("Request to save Commerce : {}", commerceDTO);
        return commerceRepository.save(commerceMapper.toEntity(commerceDTO).setIsPersisted()).map(commerceMapper::toDto);
    }

    /**
     * Partially update a commerce.
     *
     * @param commerceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CommerceDTO> partialUpdate(CommerceDTO commerceDTO) {
        log.debug("Request to partially update Commerce : {}", commerceDTO);

        return commerceRepository
            .findById(commerceDTO.getId())
            .map(existingCommerce -> {
                commerceMapper.partialUpdate(existingCommerce, commerceDTO);

                return existingCommerce;
            })
            .flatMap(commerceRepository::save)
            .map(commerceMapper::toDto);
    }

    /**
     * Get all the commerce.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CommerceDTO> findAll() {
        log.debug("Request to get all Commerce");
        return commerceRepository.findAllWithEagerRelationships().map(commerceMapper::toDto);
    }

    /**
     * Get all the commerce with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<CommerceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return commerceRepository.findAllWithEagerRelationships(pageable).map(commerceMapper::toDto);
    }

    /**
     * Returns the number of commerce available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return commerceRepository.count();
    }

    /**
     * Get one commerce by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CommerceDTO> findOne(String id) {
        log.debug("Request to get Commerce : {}", id);
        return commerceRepository.findOneWithEagerRelationships(id).map(commerceMapper::toDto);
    }

    /**
     * Delete the commerce by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Commerce : {}", id);
        return commerceRepository.deleteById(id);
    }
}
