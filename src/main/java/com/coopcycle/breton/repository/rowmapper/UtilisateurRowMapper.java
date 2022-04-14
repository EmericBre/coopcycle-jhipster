package com.coopcycle.breton.repository.rowmapper;

import com.coopcycle.breton.domain.Utilisateur;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Utilisateur}, with proper type conversions.
 */
@Service
public class UtilisateurRowMapper implements BiFunction<Row, String, Utilisateur> {

    private final ColumnConverter converter;

    public UtilisateurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Utilisateur} stored in the database.
     */
    @Override
    public Utilisateur apply(Row row, String prefix) {
        Utilisateur entity = new Utilisateur();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setFirstname(converter.fromRow(row, prefix + "_firstname", String.class));
        entity.setLastname(converter.fromRow(row, prefix + "_lastname", String.class));
        entity.setMail(converter.fromRow(row, prefix + "_mail", String.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        return entity;
    }
}
