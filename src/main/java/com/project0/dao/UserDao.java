package com.project0.dao;

import java.sql.*;

import com.project0.model.User;
import com.project0.sql.Sql;

/**
 * TransactionDao provides a way to access the Transaction table of the database
 * while seperating the application layer from the persistence layer.
 *
 */
public class UserDao {

    /**
     * create adds a new User to the database
     * 
     * @param user is the User being added to the DB
     * @return is true if the User was created succesfully
     */
    public boolean create(User user) {

        String query = "INSERT INTO \"User\" VALUES (DEFAULT,?,?,?,?,?);";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setBytes(3, user.getPasswordSalt());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            if (stmt.executeUpdate() == 0) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }
        return false;

    };

    /**
     * getById will return a user with a specific Id
     * 
     * @param id is the id that will be used to query for
     * @return is the user if they are found
     */
    public User getById(long id) {

        String query = "SELECT * FROM \"User\" WHERE id=?";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            return createUserObject(stmt.executeQuery());

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);

        }
        return null;

    };

    /**
     * getByUsername will find a user based on their unique username
     * 
     * @param username is what will be used to query for a user
     * @return is the user if they are found
     */
    public User getByUsername(String username) {
        String query = "SELECT * FROM \"User\" WHERE username=?";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            return createUserObject(stmt.executeQuery());

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }

        return null;
    }

    /**
     * createUserObject will construct user objects from resultsets
     * 
     * @param rs is a result set containing infromation that can be used to make a
     *           user object
     * @return is the resulting user object
     */
    private User createUserObject(ResultSet rs) {
        User user = new User();

        try {
            if (!rs.next())
                return null;

            user.setUserId(rs.getLong(1));
            user.setUsername(rs.getString(2));
            user.setPasswordHash(rs.getString(3));
            user.setPasswordSalt(rs.getBytes(4));
            user.setFirstName(rs.getString(5));
            user.setLastName(rs.getString(6));
            return user;
        } catch (SQLException e) {
            System.out.println("Error creating user from result set: " + e);
        }
        return null;
    }

}