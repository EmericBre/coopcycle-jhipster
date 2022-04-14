package com.coopcycle.breton.service;

import com.coopcycle.breton.domain.Utilisateur;
import com.coopcycle.breton.repository.UtilisateurRepository;
import com.coopcycle.breton.service.dto.UtilisateurDTO;
import com.coopcycle.breton.service.mapper.UtilisateurMapper;
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
 * Service Implementation for managing {@link Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UtilisateurDTO> save(UtilisateurDTO utilisateurDTO) {
        log.debug("Request to save Utilisateur : {}", utilisateurDTO);
        return utilisateurRepository.save(utilisateurMapper.toEntity(utilisateurDTO)).map(utilisateurMapper::toDto);
    }

    /**
     * Update a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UtilisateurDTO> update(UtilisateurDTO utilisateurDTO) {
        log.debug("Request to save Utilisateur : {}", utilisateurDTO);
        return utilisateurRepository.save(utilisateurMapper.toEntity(utilisateurDTO).setIsPersisted()).map(utilisateurMapper::toDto);
    }

    /**
     * Partially update a utilisateur.
     *
     * @param utilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO) {
        log.debug("Request to partially update Utilisateur : {}", utilisateurDTO);

        return utilisateurRepository
            .findById(utilisateurDTO.getId())
            .map(existingUtilisateur -> {
                utilisateurMapper.partialUpdate(existingUtilisateur, utilisateurDTO);

                return existingUtilisateur;
            })
            .flatMap(utilisateurRepository::save)
            .map(utilisateurMapper::toDto);
    }

    /**
     * Get all the utilisateurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UtilisateurDTO> findAll() {
        log.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll().map(utilisateurMapper::toDto);
    }

    /**
     * Returns the number of utilisateurs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return utilisateurRepository.count();
    }

    /**
     * Get one utilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UtilisateurDTO> findOne(String id) {
        log.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id).map(utilisateurMapper::toDto);
    }

    /**
     * Delete the utilisateur by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Utilisateur : {}", id);
        return utilisateurRepository.deleteById(id);
    }
}
