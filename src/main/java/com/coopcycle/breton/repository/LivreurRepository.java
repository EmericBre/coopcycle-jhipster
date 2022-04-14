package com.coopcycle.breton.repository;

import com.coopcycle.breton.domain.Livreur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Livreur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivreurRepository extends ReactiveCrudRepository<Livreur, String>, LivreurRepositoryInternal {
    @Override
    <S extends Livreur> Mono<S> save(S entity);

    @Override
    Flux<Livreur> findAll();

    @Override
    Mono<Livreur> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface LivreurRepositoryInternal {
    <S extends Livreur> Mono<S> save(S entity);

    Flux<Livreur> findAllBy(Pageable pageable);

    Flux<Livreur> findAll();

    Mono<Livreur> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Livreur> findAllBy(Pageable pageable, Criteria criteria);

}
