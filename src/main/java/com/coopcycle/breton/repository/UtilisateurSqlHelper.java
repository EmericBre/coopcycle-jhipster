package com.coopcycle.breton.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UtilisateurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("firstname", table, columnPrefix + "_firstname"));
        columns.add(Column.aliased("lastname", table, columnPrefix + "_lastname"));
        columns.add(Column.aliased("mail", table, columnPrefix + "_mail"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));

        return columns;
    }
}
