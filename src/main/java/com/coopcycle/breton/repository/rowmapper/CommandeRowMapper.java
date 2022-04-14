package com.coopcycle.breton.repository.rowmapper;

import com.coopcycle.breton.domain.Commande;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commande}, with proper type conversions.
 */
@Service
public class CommandeRowMapper implements BiFunction<Row, String, Commande> {

    private final ColumnConverter converter;

    public CommandeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commande} stored in the database.
     */
    @Override
    public Commande apply(Row row, String prefix) {
        Commande entity = new Commande();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setUtilisateurId(converter.fromRow(row, prefix + "_utilisateur_id", String.class));
        entity.setLivreurId(converter.fromRow(row, prefix + "_livreur_id", String.class));
        return entity;
    }
}
