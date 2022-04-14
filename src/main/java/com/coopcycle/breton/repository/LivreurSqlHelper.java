package com.coopcycle.breton.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LivreurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("firstname", table, columnPrefix + "_firstname"));
        columns.add(Column.aliased("lastname", table, columnPrefix + "_lastname"));
        columns.add(Column.aliased("phone", table, columnPrefix + "_phone"));

        return columns;
    }
}
