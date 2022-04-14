package com.coopcycle.breton.repository;

import com.coopcycle.breton.domain.Produit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Produit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProduitRepository extends ReactiveCrudRepository<Produit, String>, ProduitRepositoryInternal {
    @Override
    Mono<Produit> findOneWithEagerRelationships(String id);

    @Override
    Flux<Produit> findAllWithEagerRelationships();

    @Override
    Flux<Produit> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM produit entity JOIN rel_produit__commande joinTable ON entity.id = joinTable.commande_id WHERE joinTable.commande_id = :id"
    )
    Flux<Produit> findByCommande(String id);

    @Query("SELECT * FROM produit entity WHERE entity.id not in (select paiement_id from paiement)")
    Flux<Produit> findAllWherePaiementIsNull();

    @Query("SELECT * FROM produit entity WHERE entity.commerce_id = :id")
    Flux<Produit> findByCommerce(String id);

    @Query("SELECT * FROM produit entity WHERE entity.commerce_id IS NULL")
    Flux<Produit> findAllWhereCommerceIsNull();

    @Override
    <S extends Produit> Mono<S> save(S entity);

    @Override
    Flux<Produit> findAll();

    @Override
    Mono<Produit> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface ProduitRepositoryInternal {
    <S extends Produit> Mono<S> save(S entity);

    Flux<Produit> findAllBy(Pageable pageable);

    Flux<Produit> findAll();

    Mono<Produit> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Produit> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Produit> findOneWithEagerRelationships(String id);

    Flux<Produit> findAllWithEagerRelationships();

    Flux<Produit> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(String id);
}
