package com.sql;

import java.sql.*;

public final class SQL {

    /**
     * queries the DB
     * 
     * @param query is the SQL qeury
     */
    public void query(String query) {

        Connection conn = null;
        Statement setupStatement = null;

        try {
            // connect to DB
            conn = getRemoteConnection();

            // create statement, execute it on DB, and close
            setupStatement = conn.createStatement();
            setupStatement.executeQuery(query);
            setupStatement.close();

        } catch (SQLException e) {

            // returned SQL
            System.out.println("SQLException: " + e.getMessage());

        } finally {

            // close connection to DB
            System.out.println("Closing the connection.");
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Creates a connection to the DB
     * 
     * @return Connection to DB
     */
    private Connection getRemoteConnection() {

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
                System.out.println("Getting remote connection with connection string from environment variables.");
                Connection conn = DriverManager.getConnection(jdbcUrl);
                System.out.println("Remote connection successful.");
                return conn;
            } catch (ClassNotFoundException e) {

                // postgres driver not found
                System.out.println("Class not found: " + e.toString());
            } catch (SQLException e) {

                // error connecting to DB
                System.out.println("SQL Error: " + e.toString());
            }
        }
        return null;
    }

}
