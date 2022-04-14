package com.coopcycle.breton.repository;

import com.coopcycle.breton.domain.Paiement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Paiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementRepository extends ReactiveCrudRepository<Paiement, Long>, PaiementRepositoryInternal {
    @Query("SELECT * FROM paiement entity WHERE entity.produit_id = :id")
    Flux<Paiement> findByProduit(Long id);

    @Query("SELECT * FROM paiement entity WHERE entity.produit_id IS NULL")
    Flux<Paiement> findAllWhereProduitIsNull();

    @Override
    <S extends Paiement> Mono<S> save(S entity);

    @Override
    Flux<Paiement> findAll();

    @Override
    Mono<Paiement> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PaiementRepositoryInternal {
    <S extends Paiement> Mono<S> save(S entity);

    Flux<Paiement> findAllBy(Pageable pageable);

    Flux<Paiement> findAll();

    Mono<Paiement> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Paiement> findAllBy(Pageable pageable, Criteria criteria);

}
