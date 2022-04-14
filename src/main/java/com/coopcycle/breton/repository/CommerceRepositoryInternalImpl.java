package com.coopcycle.breton.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.coopcycle.breton.domain.Commerce;
import com.coopcycle.breton.domain.Cooperative;
import com.coopcycle.breton.repository.rowmapper.CommerceRowMapper;
import com.coopcycle.breton.repository.rowmapper.UtilisateurRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Commerce entity.
 */
@SuppressWarnings("unused")
class CommerceRepositoryInternalImpl extends SimpleR2dbcRepository<Commerce, String> implements CommerceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UtilisateurRowMapper utilisateurMapper;
    private final CommerceRowMapper commerceMapper;

    private static final Table entityTable = Table.aliased("commerce", EntityManager.ENTITY_ALIAS);
    private static final Table utilisateurTable = Table.aliased("utilisateur", "utilisateur");

    private static final EntityManager.LinkTable cooperativeLink = new EntityManager.LinkTable(
        "rel_commerce__cooperative",
        "commerce_id",
        "cooperative_id"
    );

    public CommerceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UtilisateurRowMapper utilisateurMapper,
        CommerceRowMapper commerceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Commerce.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.utilisateurMapper = utilisateurMapper;
        this.commerceMapper = commerceMapper;
    }

    @Override
    public Flux<Commerce> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Commerce> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CommerceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UtilisateurSqlHelper.getColumns(utilisateurTable, "utilisateur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(utilisateurTable)
            .on(Column.create("utilisateur_id", entityTable))
            .equals(Column.create("id", utilisateurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Commerce.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Commerce> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Commerce> findById(String id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Commerce> findOneWithEagerRelationships(String id) {
        return findById(id);
    }

    @Override
    public Flux<Commerce> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Commerce> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Commerce process(Row row, RowMetadata metadata) {
        Commerce entity = commerceMapper.apply(row, "e");
        entity.setUtilisateur(utilisateurMapper.apply(row, "utilisateur"));
        return entity;
    }

    @Override
    public <S extends Commerce> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Commerce> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(cooperativeLink, entity.getId(), entity.getCooperatives().stream().map(Cooperative::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(String entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(String entityId) {
        return entityManager.deleteFromLinkTable(cooperativeLink, entityId);
    }
}
