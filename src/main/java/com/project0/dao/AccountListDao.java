package com.project0.dao;

import revatureCollections.VanquishList;
import java.sql.*;

import com.project0.model.AccountList;
import com.project0.sql.Sql;

/**
 * AccountListDao provides a way to access the AccountList table of the database
 * while seperating the application layer from the persistence layer.
 *
 */
public class AccountListDao {
    /**
     * create will create a new entry in AccountList
     * 
     * @param accountList is the accountlist that will be added to the db
     * @return true if the creation was successful
     */
    public boolean create(AccountList accountList) {
        String query = "INSERT INTO \"AccountList\" VALUES (DEFAULT,?,?);";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, accountList.getUserId());
            stmt.setLong(2, accountList.getAccountId());

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
     * getAccountById will get every account which matches an AccountId
     * 
     * @param accountId is the id that will be used to query the db for Accountlists
     * @return is a list of all matching IDs
     */
    public VanquishList<AccountList> getByAccountId(long accountId) {
        VanquishList<AccountList> listOfAccounts = new VanquishList<AccountList>();

        String query = "SELECT * FROM \"AccountList\" WHERE \"accountId\"=?";
        AccountList accountList;

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, accountId);

            ResultSet rs = stmt.executeQuery();
            while ((accountList = createAccountListObject(rs)) != null)
                listOfAccounts.add(accountList);

            return listOfAccounts;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }

        return null;
    }

    /**
     * getByUserId will get every account which matches a userId
     * 
     * @param userId is an id of any user with a db
     * @return is a list of every account which a user has
     */
    public VanquishList<AccountList> getByUserId(long userId) {
        VanquishList<AccountList> listOfAccounts = new VanquishList<AccountList>();

        String query = "SELECT * FROM \"AccountList\" WHERE \"userId\"=?";
        AccountList accountList;

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            while ((accountList = createAccountListObject(rs)) != null)
                listOfAccounts.add(accountList);

            return listOfAccounts;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }

        return null;
    }

    /**
     * createAccountListObject will construct objects from resultsets
     * 
     * @param rs is a result set containing infromation that can be used to make a
     *           AccountList object
     * @return is the resulting AccountList object
     */
    private AccountList createAccountListObject(ResultSet rs) {
        AccountList accountList = new AccountList();

        try {
            if (!rs.next())
                return null;

            accountList.setId(rs.getInt(1));
            accountList.setUserId(rs.getInt(2));
            accountList.setAccountId(rs.getInt(3));

            return accountList;
        } catch (SQLException e) {
            System.out.println("Error creating account from result set: " + e);
        }
        return null;
    }
}
