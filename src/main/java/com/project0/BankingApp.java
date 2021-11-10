package com.project0;

import com.project0.service.MenuMessages;
import com.project0.service.MenuOptions;

/**
 * 
 * Banking app is an app that allows you to create a bank account, log in and
 * out, send money between accounts, create joint accounts, and much more!
 * 
 */
public final class BankingApp {

    private MenuOptions menuOptions;
    private MenuMessages menuMessages;

    /**
     * BankingApp constructor
     */
    public BankingApp() {
        this.menuMessages = new MenuMessages();
        this.menuOptions = new MenuOptions(this.menuMessages);
    }

    // main
    public static void main(String[] args) {
        BankingApp app = new BankingApp();

        app.startApp();

    }

    /**
     * intializes the program and directs the user to the login message
     */
    public void startApp() {
        menuMessages.welcomeMessage();
        menuOptions.loginMenu();
    }
}
