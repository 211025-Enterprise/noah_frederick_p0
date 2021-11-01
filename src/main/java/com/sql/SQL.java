package com.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SQL {

    /**
     * queries the DB for all users
     * 
     * @return is a list of strings of all the users
     */
    public List<String> getUserList() throws SQLException {

        List<String> res = new ArrayList<String>();

        // try with resource block
        try (
                // connect to DB and create statement
                Connection conn = getRemoteConnection();
                Statement statement = conn.createStatement();

                // execute query and get result
                ResultSet rs = statement.executeQuery("SELECT username FROM \"User\";");) {

            while (rs.next())
                res.add(rs.getString(1));
            return res;
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
        // return null if no connection was made
        return null;
    }

}
