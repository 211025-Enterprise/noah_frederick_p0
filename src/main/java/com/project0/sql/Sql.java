package com.project0.sql;

import java.sql.*;

public class Sql {

    /**
     * Creates a connection to the DB
     * 
     * @return Connection to DB
     */
    public static Connection getConnection() {

        // if DB env are setup then create connection
        if (System.getenv("RDS_HOSTNAME") != null) {
            try {
                // grab postgres driver (Maven dependency)
                Class.forName("org.postgresql.Driver");

                // grab DB connection parameters from env
                String dbName = System.getenv("RDS_DB_NAME");
                String userName = System.getenv("RDS_USERNAME");
                String password = System.getenv("RDS_PASSWORD");
                String hostname = System.getenv("RDS_HOSTNAME");
                String port = System.getenv("RDS_PORT");
                String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName
                        + "&password=" + password;

                // attempt to connect to DB
                return DriverManager.getConnection(jdbcUrl);

            } catch (ClassNotFoundException e) {

                // postgres driver not found
                System.out.println("Class not found: " + e.toString());
            } catch (SQLException e) {

                // error connecting to DB
                System.out.println("SQL Error: " + e.toString());
            }
        } else {
            System.out.println("ENV variables not found.");

        }
        // return null if no connection was made
        return null;
    }

}
