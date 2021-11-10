package com.project0.service;

import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * InputHandler deals with validating user input and returing only the desired
 * values
 *
 */
public class InputHandler {

    private final static Scanner in = new Scanner(System.in);

    /**
     * getInt gets only positive numbers, without whitespaces, from the user
     * 
     * @return is the resulting int
     */
    public static int getInt() {
        int input;

        while (!in.hasNextInt() || (input = in.nextInt()) < 0) {
            System.out.println("Please input only positive numbers!");
        }

        return input;
    }

    /**
     * getDouble gets numbers that are positive and enforces them to follow money
     * format.
     * 
     * @return is the resulting double
     */
    public static double getDouble() {
        double input;

        while (!in.hasNextDouble() || (input = in.nextDouble()) < 0) {
            System.out.println("Please input only positive numbers and format as currency!");
        }

        BigDecimal bd = BigDecimal.valueOf(input);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        in.nextLine();
        return bd.doubleValue();
    }

    /**
     * getAlphabeticString gets a string that only contains letters
     * 
     * @return is the resulting string
     */
    public static String getAlphabeticString() {
        String input;

        while (!(input = in.next()).matches("[a-zA-Z]*"))
            System.out.println("Please input only letters!");

        in.nextLine();
        return input;
    }

    /**
     * getAlphanumericString gets a string that contains only letters and numbers
     * 
     * @return is the resulting string
     */
    public static String getAlphanumericString() {
        String input;

        while (!(input = in.next()).matches("[a-zA-Z0-9]*"))
            System.out.println("Please input only letters or numbers!");

        in.nextLine();
        return input;
    }

    /**
     * getSpecialAlphanumericString gets a string that contains letters, numbers,
     * and special characters. Used for passwords.
     * 
     * @return is the resulting string
     */
    public static String getSpecialAlphanumericString() {
        String input;

        while (!(input = in.next()).matches("[a-zA-Z0-9!@#$%^&*()]*"))
            System.out.println("Please input only letters, numbers, or special characters!");

        in.nextLine();
        return input;
    }

    /**
     * getMenuSelection is a helper method that reduces code and handels all the
     * needed input for navigating menues
     * 
     * @return is the selection the user has made on a manue
     */
    public static int getMenuSelection(int numberOfOptions) {

        int userSelection;

        while ((userSelection = getInt()) > numberOfOptions || userSelection < 1) {
            System.out.println("Please enter a number from 1 to " + (numberOfOptions));
        }

        return userSelection;
    }
}
