package com.coopcycle.breton.service;

import com.coopcycle.breton.domain.Paiement;
import com.coopcycle.breton.repository.PaiementRepository;
import com.coopcycle.breton.service.dto.PaiementDTO;
import com.coopcycle.breton.service.mapper.PaiementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    public PaiementService(PaiementRepository paiementRepository, PaiementMapper paiementMapper) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
    }

    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaiementDTO> save(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        return paiementRepository.save(paiementMapper.toEntity(paiementDTO)).map(paiementMapper::toDto);
    }

    /**
     * Update a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaiementDTO> update(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        return paiementRepository.save(paiementMapper.toEntity(paiementDTO)).map(paiementMapper::toDto);
    }

    /**
     * Partially update a paiement.
     *
     * @param paiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PaiementDTO> partialUpdate(PaiementDTO paiementDTO) {
        log.debug("Request to partially update Paiement : {}", paiementDTO);

        return paiementRepository
            .findById(paiementDTO.getId())
            .map(existingPaiement -> {
                paiementMapper.partialUpdate(existingPaiement, paiementDTO);

                return existingPaiement;
            })
            .flatMap(paiementRepository::save)
            .map(paiementMapper::toDto);
    }

    /**
     * Get all the paiements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaiementDTO> findAll() {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll().map(paiementMapper::toDto);
    }

    /**
     * Returns the number of paiements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return paiementRepository.count();
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PaiementDTO> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id).map(paiementMapper::toDto);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        return paiementRepository.deleteById(id);
    }
}
