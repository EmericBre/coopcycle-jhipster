package com.coopcycle.breton.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.coopcycle.breton.domain.Commande;
import com.coopcycle.breton.domain.Produit;
import com.coopcycle.breton.repository.rowmapper.CommerceRowMapper;
import com.coopcycle.breton.repository.rowmapper.ProduitRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Produit entity.
 */
@SuppressWarnings("unused")
class ProduitRepositoryInternalImpl extends SimpleR2dbcRepository<Produit, String> implements ProduitRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CommerceRowMapper commerceMapper;
    private final ProduitRowMapper produitMapper;

    private static final Table entityTable = Table.aliased("produit", EntityManager.ENTITY_ALIAS);
    private static final Table commerceTable = Table.aliased("commerce", "commerce");

    private static final EntityManager.LinkTable commandeLink = new EntityManager.LinkTable(
        "rel_produit__commande",
        "produit_id",
        "commande_id"
    );

    public ProduitRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CommerceRowMapper commerceMapper,
        ProduitRowMapper produitMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Produit.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.commerceMapper = commerceMapper;
        this.produitMapper = produitMapper;
    }

    @Override
    public Flux<Produit> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Produit> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProduitSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CommerceSqlHelper.getColumns(commerceTable, "commerce"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(commerceTable)
            .on(Column.create("commerce_id", entityTable))
            .equals(Column.create("id", commerceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Produit.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Produit> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Produit> findById(String id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Produit> findOneWithEagerRelationships(String id) {
        return findById(id);
    }

    @Override
    public Flux<Produit> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Produit> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Produit process(Row row, RowMetadata metadata) {
        Produit entity = produitMapper.apply(row, "e");
        entity.setCommerce(commerceMapper.apply(row, "commerce"));
        return entity;
    }

    @Override
    public <S extends Produit> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Produit> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(commandeLink, entity.getId(), entity.getCommandes().stream().map(Commande::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(String entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(String entityId) {
        return entityManager.deleteFromLinkTable(commandeLink, entityId);
    }
}
