package com.coopcycle.breton.repository.rowmapper;

import com.coopcycle.breton.domain.Produit;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Produit}, with proper type conversions.
 */
@Service
public class ProduitRowMapper implements BiFunction<Row, String, Produit> {

    private final ColumnConverter converter;

    public ProduitRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Produit} stored in the database.
     */
    @Override
    public Produit apply(Row row, String prefix) {
        Produit entity = new Produit();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", Float.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCommerceId(converter.fromRow(row, prefix + "_commerce_id", String.class));
        return entity;
    }
}
