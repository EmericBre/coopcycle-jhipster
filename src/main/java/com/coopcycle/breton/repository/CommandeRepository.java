package com.coopcycle.breton.repository;

import com.coopcycle.breton.domain.Commande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Commande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeRepository extends ReactiveCrudRepository<Commande, String>, CommandeRepositoryInternal {
    @Query("SELECT * FROM commande entity WHERE entity.utilisateur_id = :id")
    Flux<Commande> findByUtilisateur(String id);

    @Query("SELECT * FROM commande entity WHERE entity.utilisateur_id IS NULL")
    Flux<Commande> findAllWhereUtilisateurIsNull();

    @Query("SELECT * FROM commande entity WHERE entity.livreur_id = :id")
    Flux<Commande> findByLivreur(String id);

    @Query("SELECT * FROM commande entity WHERE entity.livreur_id IS NULL")
    Flux<Commande> findAllWhereLivreurIsNull();

    @Override
    <S extends Commande> Mono<S> save(S entity);

    @Override
    Flux<Commande> findAll();

    @Override
    Mono<Commande> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface CommandeRepositoryInternal {
    <S extends Commande> Mono<S> save(S entity);

    Flux<Commande> findAllBy(Pageable pageable);

    Flux<Commande> findAll();

    Mono<Commande> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Commande> findAllBy(Pageable pageable, Criteria criteria);

}
