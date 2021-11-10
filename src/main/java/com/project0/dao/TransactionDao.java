package com.project0.dao;

import java.sql.*;

import com.project0.model.Transaction;
import com.project0.sql.Sql;

import revatureCollections.VanquishList;

/**
 * TransactionDao provides a way to access the Transaction table of the database
 * while seperating the application layer from the persistence layer.
 *
 */
public class TransactionDao {
    /**
     * create will create a new Transaction entry in the Transaction table
     * 
     * @param transaction is the accountlist that will be added to the db
     * @return true if the creation was successful
     */
    public boolean create(Transaction transaction) {
        String query = "INSERT INTO \"Transaction\" VALUES (DEFAULT,?,?,?);";

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, transaction.getAccountId());
            stmt.setLong(2, transaction.getType());
            stmt.setDouble(3, transaction.getAmount());

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
     * getByAccountId will get Transactions based on which count they're associated
     * with
     * 
     * @param accountId is the account
     * @return is a list of the Transactions
     */
    public VanquishList<Transaction> getByAccountId(long accountId) {
        VanquishList<Transaction> listOfTransactions = new VanquishList<Transaction>();

        String query = "SELECT * FROM \"Transaction\" WHERE \"accountId\"=?";
        Transaction transaction;

        try (Connection conn = Sql.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, accountId);

            ResultSet rs = stmt.executeQuery();
            while ((transaction = createATransactionObject(rs)) != null)
                listOfTransactions.add(transaction);

            return listOfTransactions;

        } catch (SQLException e) {
            System.out.println("SQL exception: " + e);
        }

        return null;
    }

    /**
     * createATransactionObject will construct Transaction objects from resultsets
     * 
     * @param rs is a result set containing infromation that can be used to make
     *           Transaction objects
     * @return is the resulting Transaction object
     */
    private Transaction createATransactionObject(ResultSet rs) {
        Transaction transaction = new Transaction();

        try {
            if (!rs.next())
                return null;

            transaction.setId(rs.getLong(1));
            transaction.setAccountId(rs.getLong(2));
            transaction.setType(rs.getInt(3));
            transaction.setAmount(rs.getDouble(4));

            return transaction;
        } catch (SQLException e) {
            System.out.println("Error creating account from result set: " + e);
        }
        return null;
    }

}
