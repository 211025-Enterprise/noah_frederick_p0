package com.project0.service;

import com.project0.model.Account;
import com.project0.model.AccountList;

import revatureCollections.VanquishList;

/**
 * MenuOptions controls the options used on every menu within the program and
 * directs the users on where they go next
 */
public class MenuOptions {

    public UserService userService;
    public AccountService accountService;
    private MenuMessages menuMessages;

    public MenuOptions(MenuMessages menuMessages) {
        this.menuMessages = menuMessages;
        this.userService = new UserService();
        this.accountService = new AccountService(this.userService);

    }

    /**
     * loginMenu is the inital menu when the app begins and handels login, creating
     * a user, and quitting the app
     * 
     */
    public void loginMenu() {

        while (true) {
            String[] menuOptions = { "Create a new user.", "Login in as an existing user.", "Exit LMMC BANKING app." };

            displayMenuOptions(menuOptions);

            switch (InputHandler.getMenuSelection(menuOptions.length)) {
            case 1:
                menuMessages.createUserMessage();
                userService.createUser();
                menuMessages.createAccountMessage();
                accountService.createAccount(accountCreationMenu());
                break;
            case 2:
                menuMessages.loginMessage();
                userService.login();
                menuMessages.bankingWelcomeMessage(userService.getCurrentUser());
                bankingMenu();
                break;
            case 3:
                menuMessages.exitMessage();
                return;
            }
        }
    }

    /**
     * bankingMenu is what the user can do once they've logged in and can see their
     * bank account
     */
    private void bankingMenu() {

        if (accountService.getListOfAccounts().getSize() == 0) {
            menuMessages.createAccountMessage();
            accountService.createAccount(accountCreationMenu());
        }

        while (true) {
            String[] menuOptions = { "View Balance and Transaction History.", "Withdraw/Deposit/Send/Transfer Money.",
                    "Create a New Account.", "Create a joint account", "Logout." };

            displayMenuOptions(menuOptions);

            switch (InputHandler.getMenuSelection(menuOptions.length)) {
            case 1:
                menuMessages.viewAccountDetailsMessage();
                accountService.viewAccountDetails(accountSelectionMenu());
                break;
            case 2:
                menuMessages.transactionSelectionMessage();
                transactionSelectionMenu(accountSelectionMenu());
                break;
            case 3:
                menuMessages.createAccountMessage();
                accountService.createAccount(accountCreationMenu());
                break;
            case 4:
                menuMessages.createJointAccountMessage();
                accountService.createJointAccount(accountSelectionMenu());
                break;
            case 5:
                return;
            }
        }
    }

    /**
     * transactionSelectionMenu is where the user can decided if they want to
     * deposit/withdraw/transfer/or send money from an account
     * 
     * @param accountId is the id of the account that will be transferred from
     */
    private void transactionSelectionMenu(long accountId) {
        String[] menuOptions = { "Withdraw Money", "Deposit Money", "Send Money", "Transfer Money" };

        displayMenuOptions(menuOptions);

        System.out.println("What action do you want to take?");

        switch (InputHandler.getMenuSelection(menuOptions.length)) {
        case 1:
            menuMessages.withdrawMoneyMessage();
            accountService.withdrawMoney(accountId);
            break;
        case 2:
            menuMessages.depositMoneyMessage();
            accountService.depositMoney(accountId);
            break;
        case 3:
            menuMessages.sendMoneyMessage();
            accountService.sendMoney(accountId);
            break;
        case 4:
            if (accountService.getListOfAccounts().getSize() == 1) {
                System.out.println("Sorry, you don't have enough accounts yet!");
                break;
            }
            menuMessages.transferMoneyMessage();
            long recieverAccountId = accountSelectionMenu();

            accountService.transferMoney(accountId, recieverAccountId);

        }
    }

    /**
     * accountCreationMenu controls what options the users have for their bank
     * accounts
     * 
     * @return is the selected option
     */
    private int accountCreationMenu() {
        String[] menuOptions = { "Checkings Account", "Savings Account", "Brokerage Account" };

        displayMenuOptions(menuOptions);

        return InputHandler.getMenuSelection(menuOptions.length);
    }

    /**
     * accountSelectionMenu lets the user select which account they are going to
     * interface with once they decide to draw/deposit/etc from their accounts
     * 
     * @return is the selected account
     */
    private long accountSelectionMenu() {

        VanquishList<AccountList> list = accountService.getListOfAccounts();
        String[] menuOptions = new String[list.getSize()];
        for (int i = 0; i < list.getSize(); i++) {
            Account account = accountService.getAccountbyAccountId(list.get(i).getAccountId());

            menuOptions[i] = account.getName() + " - " + accountService.getAccountTypeName(account.getType());
        }

        displayMenuOptions(menuOptions);

        return list.get(InputHandler.getMenuSelection(menuOptions.length) - 1).getAccountId();

    }

    /**
     * displayMenuOptions is a helperfunction that displays all the menu options
     * 
     * @param menuOptions is the options to be displayed
     */
    private void displayMenuOptions(String[] menuOptions) {
        for (int i = 1; i <= menuOptions.length; i++) {
            System.out.println(i + ". " + menuOptions[i - 1]);
        }
        System.out.println(" ");
    }
}
