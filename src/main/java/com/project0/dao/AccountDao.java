package com.project0.dao;

import java.sql.*;

import com.project0.model.Account;
import com.project0.sql.Sql;

/**
 * AccountDao provides a way to access the Account table of the database while
 * seperating the application layer from the persistence layer.
 *
 */
public class AccountDao {

    /**
     * create adds a new Account to the database
     * 
     * @param account is the account being added to the DB
     * @return is true if the Account was created succesfully
     */
    public boolean create(Account account) {
        String query = "INSERT INTO \"Account\" VALUES (DEFAULT,?,?,DEFAULT) RETURNING id;";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, account.getType());
            stmt.setString(2, account.getName());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                account.setId(rs.getLong(1));
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }
        return false;

    }

    /**
     * getById grabs a specifc user from the db based on their ID
     * 
     * @param id is the id used when retrieving a specific index in the db
     * @return is the account from the index
     */
    public Account getById(long id) {
        String query = "SELECT * FROM \"Account\" WHERE \"id\"=?;";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            return createAccountObject(stmt.executeQuery());

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }

        return null;
    }

    /**
     * update will update any Account in the db
     * 
     * @param account is the Account object that will be used to update the db
     * @return true if the udapte was successful
     */
    public boolean update(Account account) {

        String query = "UPDATE \"Account\" SET \"type\"=?, \"name\"=?, \"balance\"=? WHERE \"id\"=?;";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, account.getType());
            stmt.setString(2, account.getName());
            stmt.setDouble(3, account.getBalance());
            stmt.setLong(4, account.getId());

            if (stmt.executeUpdate() == 0) {
                return false;
            }
            return true;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }
        return false;
    }

    /**
     * createAccountObject will construct objects from resultsets
     * 
     * @param rs is a result set containing infromation that can be used to make a
     *           Account object
     * @return is the resulting Account object
     */
    private Account createAccountObject(ResultSet rs) {
        Account account = new Account();

        try {
            if (!rs.next())
                return null;

            account.setId(rs.getLong(1));
            account.setType(rs.getInt(2));
            account.setName(rs.getString(3));
            account.setBalance(rs.getDouble(4));

            return account;
        } catch (SQLException e) {
            System.out.println("Error creating account from result set: " + e);
        }
        return null;
    }
}
