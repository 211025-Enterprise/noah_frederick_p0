package com.project0.service;

import com.project0.model.User;

/**
 * MenuMessages holds every message that is called often or that doesn't need to
 * written alongside the program's logic
 * 
 */
public class MenuMessages {

    // Welcome Message
    public void welcomeMessage() {
        System.out.println("Welcome to...");

        slowPrint("\n/////////////////\n ************** \n  LMMC BANKING  \n ************** \n/////////////////\n\n");

        System.out.println("How may we serivce you today?\n");

    }

    // Message when creaing a user
    public void createUserMessage() {
        slowPrint("\nLMMC BANKING! A global bank with a local feel.\n\n");

        System.out.println("Before getting started, we're going to need a few details!");
    }

    // Message when logging in
    public void loginMessage() {
        slowPrint("\nLMMC BANKING! The fresh approach to banking.\n\n");

        System.out.println("It's good to have you back!\n");
    }

    // Message that appears when leaving the program
    public void exitMessage() {

        System.out.println("Thank you for banking with LMMC BANKING!");
    }

    // Message when viewing transaction history and account balance
    public void viewAccountDetailsMessage() {
        System.out.println("Please select an account.");
    }

    // Message when creating a new account
    public void createAccountMessage() {
        System.out.println("What account type do you want to create?");
    }

    // Message that appears after logging in to the bank
    public void bankingWelcomeMessage(User currentUser) {
        System.out.println("\nWelcome, " + currentUser.getFirstName() + " " + currentUser.getLastName()
                + "\nWhat would you like to do today? \n");
    }

    // Message for choosing to deposit/withdraw
    public void transactionSelectionMessage() {
        System.out.println("Which account would you like to draw/deposit from?");

    }

    // Message for creating a joint account
    public void createJointAccountMessage() {
        System.out.println("Which account do you want to add another person to?");

    }

    // Message for withdrawing money
    public void withdrawMoneyMessage() {
        System.out.println("Which account do you want to withdraw money from?");

    }

    // Message for depositing money
    public void depositMoneyMessage() {
        System.out.println("Which account do you want to deposit money into?");

    }

    // Message when transferring money between accounts
    public void transferMoneyMessage() {
        System.out.println("Which account do you want to transfer your money to?");

    }

    private void slowPrint(String message) {
        for (int i = 0; i < message.length(); i++) {
            System.out.printf("%c", message.charAt(i));
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
