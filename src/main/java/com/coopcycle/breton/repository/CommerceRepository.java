package com.coopcycle.breton.repository;

import com.coopcycle.breton.domain.Commerce;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Commerce entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommerceRepository extends ReactiveCrudRepository<Commerce, String>, CommerceRepositoryInternal {
    @Override
    Mono<Commerce> findOneWithEagerRelationships(String id);

    @Override
    Flux<Commerce> findAllWithEagerRelationships();

    @Override
    Flux<Commerce> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM commerce entity JOIN rel_commerce__cooperative joinTable ON entity.id = joinTable.cooperative_id WHERE joinTable.cooperative_id = :id"
    )
    Flux<Commerce> findByCooperative(String id);

    @Query("SELECT * FROM commerce entity WHERE entity.utilisateur_id = :id")
    Flux<Commerce> findByUtilisateur(String id);

    @Query("SELECT * FROM commerce entity WHERE entity.utilisateur_id IS NULL")
    Flux<Commerce> findAllWhereUtilisateurIsNull();

    @Override
    <S extends Commerce> Mono<S> save(S entity);

    @Override
    Flux<Commerce> findAll();

    @Override
    Mono<Commerce> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface CommerceRepositoryInternal {
    <S extends Commerce> Mono<S> save(S entity);

    Flux<Commerce> findAllBy(Pageable pageable);

    Flux<Commerce> findAll();

    Mono<Commerce> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Commerce> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Commerce> findOneWithEagerRelationships(String id);

    Flux<Commerce> findAllWithEagerRelationships();

    Flux<Commerce> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(String id);
}
