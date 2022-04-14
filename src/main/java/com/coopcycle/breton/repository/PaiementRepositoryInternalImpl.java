package com.coopcycle.breton.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.coopcycle.breton.domain.Paiement;
import com.coopcycle.breton.repository.rowmapper.PaiementRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Paiement entity.
 */
@SuppressWarnings("unused")
class PaiementRepositoryInternalImpl extends SimpleR2dbcRepository<Paiement, Long> implements PaiementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProduitRowMapper produitMapper;
    private final PaiementRowMapper paiementMapper;

    private static final Table entityTable = Table.aliased("paiement", EntityManager.ENTITY_ALIAS);
    private static final Table produitTable = Table.aliased("produit", "produit");

    public PaiementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProduitRowMapper produitMapper,
        PaiementRowMapper paiementMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Paiement.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.produitMapper = produitMapper;
        this.paiementMapper = paiementMapper;
    }

    @Override
    public Flux<Paiement> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Paiement> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PaiementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProduitSqlHelper.getColumns(produitTable, "produit"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(produitTable)
            .on(Column.create("produit_id", entityTable))
            .equals(Column.create("id", produitTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Paiement.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Paiement> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Paiement> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Paiement process(Row row, RowMetadata metadata) {
        Paiement entity = paiementMapper.apply(row, "e");
        entity.setProduit(produitMapper.apply(row, "produit"));
        return entity;
    }

    @Override
    public <S extends Paiement> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
