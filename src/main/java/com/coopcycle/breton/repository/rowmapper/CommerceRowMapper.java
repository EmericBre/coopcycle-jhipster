package com.coopcycle.breton.repository.rowmapper;

import com.coopcycle.breton.domain.Commerce;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commerce}, with proper type conversions.
 */
@Service
public class CommerceRowMapper implements BiFunction<Row, String, Commerce> {

    private final ColumnConverter converter;

    public CommerceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commerce} stored in the database.
     */
    @Override
    public Commerce apply(Row row, String prefix) {
        Commerce entity = new Commerce();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setAdress(converter.fromRow(row, prefix + "_adress", String.class));
        entity.setUtilisateurId(converter.fromRow(row, prefix + "_utilisateur_id", String.class));
        return entity;
    }
}
