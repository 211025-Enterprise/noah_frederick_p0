package com.project0;

import com.sql.SQL;

public final class App {
    public static void main(String[] args) throws Exception {
        SQL sql = new SQL();
        String query = "CREATE TABLE Test (ColumnName INT);";
        sql.query(query);
    }

}
