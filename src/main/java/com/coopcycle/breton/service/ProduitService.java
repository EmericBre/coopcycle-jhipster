package com.coopcycle.breton.service;

import com.coopcycle.breton.domain.Produit;
import com.coopcycle.breton.repository.ProduitRepository;
import com.coopcycle.breton.service.dto.ProduitDTO;
import com.coopcycle.breton.service.mapper.ProduitMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Produit}.
 */
@Service
@Transactional
public class ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitService.class);

    private final ProduitRepository produitRepository;

    private final ProduitMapper produitMapper;

    public ProduitService(ProduitRepository produitRepository, ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.produitMapper = produitMapper;
    }

    /**
     * Save a produit.
     *
     * @param produitDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProduitDTO> save(ProduitDTO produitDTO) {
        log.debug("Request to save Produit : {}", produitDTO);
        return produitRepository.save(produitMapper.toEntity(produitDTO)).map(produitMapper::toDto);
    }

    /**
     * Update a produit.
     *
     * @param produitDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProduitDTO> update(ProduitDTO produitDTO) {
        log.debug("Request to save Produit : {}", produitDTO);
        return produitRepository.save(produitMapper.toEntity(produitDTO).setIsPersisted()).map(produitMapper::toDto);
    }

    /**
     * Partially update a produit.
     *
     * @param produitDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProduitDTO> partialUpdate(ProduitDTO produitDTO) {
        log.debug("Request to partially update Produit : {}", produitDTO);

        return produitRepository
            .findById(produitDTO.getId())
            .map(existingProduit -> {
                produitMapper.partialUpdate(existingProduit, produitDTO);

                return existingProduit;
            })
            .flatMap(produitRepository::save)
            .map(produitMapper::toDto);
    }

    /**
     * Get all the produits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProduitDTO> findAll() {
        log.debug("Request to get all Produits");
        return produitRepository.findAllWithEagerRelationships().map(produitMapper::toDto);
    }

    /**
     * Get all the produits with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ProduitDTO> findAllWithEagerRelationships(Pageable pageable) {
        return produitRepository.findAllWithEagerRelationships(pageable).map(produitMapper::toDto);
    }

    /**
     *  Get all the produits where Paiement is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProduitDTO> findAllWherePaiementIsNull() {
        log.debug("Request to get all produits where Paiement is null");
        return produitRepository.findAllWherePaiementIsNull().map(produitMapper::toDto);
    }

    /**
     * Returns the number of produits available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return produitRepository.count();
    }

    /**
     * Get one produit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProduitDTO> findOne(String id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findOneWithEagerRelationships(id).map(produitMapper::toDto);
    }

    /**
     * Delete the produit by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Produit : {}", id);
        return produitRepository.deleteById(id);
    }
}
