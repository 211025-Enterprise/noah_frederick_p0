package com.project0.service;

import com.project0.dao.AccountDao;
import com.project0.dao.AccountListDao;
import com.project0.dao.TransactionDao;
import com.project0.model.User;
import com.project0.dao.UserDao;
import com.project0.model.Account;
import com.project0.model.AccountList;
import com.project0.model.Transaction;

import revatureCollections.VanquishList;

/**
 * Account service handels all of the logic for bank accounts. This includes
 * sending money, displaying account information, and creating new accounts.
 * 
 */
public class AccountService {

    private AccountListDao accountListDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private UserDao userDao;
    private UserService userService;

    /**
     * AccountService constructor
     * 
     * @param userService is an object which contains the current user
     */
    public AccountService(UserService userService) {
        this.accountDao = new AccountDao();
        this.accountListDao = new AccountListDao();
        this.transactionDao = new TransactionDao();
        this.userDao = userService.userDao;
        this.userService = userService;

    }

    /**
     * createAccount is responsible for all the logic behind creating new bank
     * accounts
     * 
     * @param accountType is the type of account being made
     *                    (brokerage/savings/checkings)
     */
    public void createAccount(int accountType) {
        Account account = new Account();
        AccountList accountList = new AccountList();

        System.out.println("Name of the account?");
        account.setName(InputHandler.getAlphanumericString());
        account.setType(accountType);

        if (accountDao.create(account))
            System.out.println("Account created successfully!");
        else {
            System.out.println("Failed to create account!");
        }

        accountList.setUserId(userService.getCurrentUser().getUserId());
        accountList.setAccountId(account.getId());

        if (accountListDao.create(accountList))
            System.out.println("Account added to list successfully!");
        else {
            System.out.println("Failed to add account to list!");
        }
    }

    /**
     * viewAccountDetails displays all of the details about a bank account.
     * 
     * @param accountId the account to display all the details about
     */
    public void viewAccountDetails(long accountId) {

        Account account = getAccountbyAccountId(accountId);
        VanquishList<Transaction> transactions = transactionDao.getByAccountId(accountId);
        VanquishList<AccountList> accountList = accountListDao.getByAccountId(accountId);

        System.out.println("Name: " + account.getName() + "\nType: " + getAccountTypeName(account.getType())
                + "\nAccount Number: " + accountId + "\nBalance: " + account.getBalance() + "\nShared with:");

        for (int i = 0; i < accountList.getSize(); i++) {
            User user = userDao.getById(accountList.get(i).getUserId());
            System.out.println((i + 1) + ". " + user.getFirstName() + " " + user.getLastName());
        }

        System.out.println("Transaction History\n====================");

        for (int i = 0; i < transactions.getSize(); i++) {
            Transaction transaction = transactions.get(i);
            System.out.println("Id: " + transaction.getId() + " Type: " + getTransactionTypeName(transaction.getType())
                    + " Amount: "
                    + (transaction.getType() > 1 ? "+$" + transaction.getAmount() : "-$" + transaction.getAmount()));
        }
        System.out.println("\n\n");
    }

    /**
     * getListOfAccounts will get a list of all current accounts the current user
     * has
     * 
     * @return is the list of the current user's accounts
     */
    public VanquishList<AccountList> getListOfAccounts() {

        return accountListDao.getByUserId(userService.getCurrentUser().getUserId());
    }

    /**
     * getaccountByAccountID will get an accout with a certain id
     * 
     * @param accountId is the id
     * @return is the account
     */
    public Account getAccountbyAccountId(long accountId) {
        return accountDao.getById(accountId);
    }

    /**
     * createJointAccount is responsible for the logic behind creating join accounts
     * 
     * @param accountId is the id for the account that will become a joint account
     */
    public void createJointAccount(long accountId) {
        String username;
        AccountList accountList = new AccountList();

        System.out.println("Please enter the username of the person you would like to add to the account.");
        while (userDao.getByUsername(username = InputHandler.getAlphanumericString()) == null)
            System.out.println("Could not find a user with that name!");

        accountList.setUserId(userDao.getByUsername(username).getUserId());
        accountList.setAccountId(accountId);

        accountListDao.create(accountList);
    }

    /**
     * getAccountTypeName will return the type of an account in string form
     * 
     * @param type is the numeric type
     * @return is the string representation
     */
    public String getAccountTypeName(int type) {
        String accountTypeName = "404 not found";
        switch (type) {
        case 1:
            accountTypeName = "Checkings Account";
            break;
        case 2:
            accountTypeName = "Savings Account";
            break;
        case 3:
            accountTypeName = "Brokerage Account";

        }
        return accountTypeName;
    }

    /**
     * deposit money is reponsible for the logic used when depositing money into the
     * account
     * 
     * @param accountId is the id of the account which is being deposited into
     */
    public void depositMoney(long accountId) {
        int depositTransaction = 1;

        Account account = getAccountbyAccountId(accountId);
        System.out.println("How much would you like to deposit?");

        double amount = InputHandler.getDouble();
        account.setBalance(account.getBalance() + amount);

        if (accountDao.update(account)) {
            System.out.println("Successfully deposited the money!");
        } else {
            System.out.println("There was an error completing the transaction.");
        }

        transactionDao.create(new Transaction(accountId, depositTransaction, amount));

    }

    /**
     * withdrawMoney is reponsible for the logic used when withdrawing money from
     * the account
     * 
     * @param accountId is the id of the account which is being withdrawn
     */
    public void withdrawMoney(long accountId) {
        int withdrawTransaction = 2;

        Account account = getAccountbyAccountId(accountId);
        System.out.println("How much would you like to withdraw?");

        double amount = getAmount(account);
        account.setBalance(account.getBalance() - amount);

        if (accountDao.update(account)) {
            System.out.println("Successfully withdrew the money!");
        } else {
            System.out.println("There was an error completing the transaction.");
        }

        transactionDao.create(new Transaction(accountId, withdrawTransaction, amount));

    }

    /**
     * sendMoney is responsible for the logic used when sending money from one
     * account to another
     * 
     * @param accountId is the id of the account which money is being sent from
     */
    public void sendMoney(long senderAccountId) {
        int depositTransaction = 1;
        int transferTransaction = 3;

        int recieverAccountId;

        System.out.println("Please enter the account number of the recieving account.");

        while (getAccountbyAccountId(recieverAccountId = InputHandler.getInt()) == null)
            System.out.println("There was no account found with that number!");

        Account senderAccount = getAccountbyAccountId(senderAccountId);
        Account recieverAccount = getAccountbyAccountId(recieverAccountId);

        System.out.println("How much would you like to send?");

        double amount = getAmount(senderAccount);
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        recieverAccount.setBalance(recieverAccount.getBalance() + amount);

        if (accountDao.update(senderAccount) && accountDao.update(recieverAccount)) {
            System.out.println("Successfully sent the money!");
        } else {
            System.out.println("Unable to process the transaction!");
        }

        transactionDao.create(new Transaction(recieverAccountId, depositTransaction, amount));
        transactionDao.create(new Transaction(senderAccountId, transferTransaction, amount));

    }

    /**
     * transferMoney is reponsible for the logic used when transferring money from
     * one account to anothe
     * 
     * @param accountId is the id of the account from which money is being
     *                  transferred out of
     */
    public void transferMoney(long senderAccountId, long recieverAccountId) {
        int depositTransaction = 1;
        int transferTransaction = 3;

        Account senderAccount = getAccountbyAccountId(senderAccountId);
        Account recieverAccount = getAccountbyAccountId(recieverAccountId);

        System.out.println("How much would you like to transfer?");
        double amount = getAmount(senderAccount);

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        recieverAccount.setBalance(recieverAccount.getBalance() + amount);

        if (accountDao.update(senderAccount) && accountDao.update(recieverAccount)) {
            System.out.println("Successfully sent the money!");
        } else {
            System.out.println("Unable to process the transaction!");
        }

        transactionDao.create(new Transaction(recieverAccountId, depositTransaction, amount));
        transactionDao.create(new Transaction(senderAccountId, transferTransaction, amount));
    }

    /**
     * getAmount will determine if theres enough money in an account to complete a
     * transaction
     * 
     * @param account is the account where the transaction is taking place
     * @return true if the transaction is possible
     */
    private double getAmount(Account account) {
        while (true) {
            double amount = InputHandler.getDouble();
            if (account.getBalance() - amount < 0) {
                System.out.println(
                        "You do not have enough funds to cover this transaction!\nPlease enter a lower amount.\n");
            } else {
                return amount;
            }
        }
    }

    /**
     * getTransactionTypeName will return the string representation of the
     * transaction types
     * 
     * @param type is the numeric representation
     * @return is the returned string representation
     */
    private String getTransactionTypeName(int type) {
        String accountTypeName = "404 not found";
        switch (type) {
        case 1:
            accountTypeName = "Deposit";
            break;
        case 2:
            accountTypeName = "Withdraw";
            break;
        case 3:
            accountTypeName = "Send";
        }
        return accountTypeName;
    }

}
