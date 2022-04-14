package com.coopcycle.breton.repository.rowmapper;

import com.coopcycle.breton.domain.Cooperative;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cooperative}, with proper type conversions.
 */
@Service
public class CooperativeRowMapper implements BiFunction<Row, String, Cooperative> {

    private final ColumnConverter converter;

    public CooperativeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cooperative} stored in the database.
     */
    @Override
    public Cooperative apply(Row row, String prefix) {
        Cooperative entity = new Cooperative();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAdress(converter.fromRow(row, prefix + "_adress", String.class));
        return entity;
    }
}
