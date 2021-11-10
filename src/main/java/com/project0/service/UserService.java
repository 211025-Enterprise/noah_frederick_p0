package com.project0.service;

import com.project0.dao.UserDao;
import com.project0.model.User;

import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;

import java.security.*;
import java.security.spec.*;

import org.apache.commons.codec.binary.Hex;

/**
 * UserService handles code relating strictly to the users. LoginCreating
 * accounts, etc.
 * 
 */
public class UserService {

    public UserDao userDao;
    private Random r;
    private User currentUser;

    /**
     * Constructor
     */
    public UserService() {
        this.userDao = new UserDao();
        this.r = new SecureRandom();
        this.currentUser = new User();
    }

    /**
     * createUser is a method which is responsible for creating new Users in the
     * program. It handels Usernames, Password salt&hashs, credentials, etc
     * 
     */
    public void createUser() {

        String username;
        String passwordHash;
        byte[] salt = new byte[16];
        r.nextBytes(salt);
        System.out.println("User Name:");

        while (userDao.getByUsername(username = InputHandler.getAlphanumericString()) != null)
            System.out.println("Username is already taken! Please choose another name!");
        currentUser.setUsername(username);

        System.out.println("Password:");
        while ((passwordHash = hashPassword(InputHandler.getSpecialAlphanumericString(), salt)) == null) {
            System.out.println("Error saving password! Please try again!");
        }
        currentUser.setPasswordHash(passwordHash);
        currentUser.setPasswordSalt(salt);

        System.out.println("First Name:");
        currentUser.setFirstName(InputHandler.getAlphabeticString());

        System.out.println("Last Name:");
        currentUser.setLastName(InputHandler.getAlphabeticString());

        if (userDao.create(currentUser)) {
            System.out.println("User Created Successfully!");
            currentUser.setUserId(userDao.getByUsername(username).getUserId());
        }

        else {
            System.out.println("Failed to create user!");
        }
    }

    /**
     * login is what is responsible for logging the user in and validating
     * credentials
     * 
     */
    public void login() {

        while (true) {
            System.out.println("User Name:");
            String username = InputHandler.getAlphanumericString();

            System.out.println("Password:");
            String password = InputHandler.getSpecialAlphanumericString();

            if (doLoginCredsMatch(username, password)) {
                System.out.println("\nLogged In Successfully! Sending you to banking...");
                break;

            } else {
                System.out.println("Username or password is incorrect! Please try again!");

            }
        }
    }

    /**
     * getCurrentUser is a getter used for allowing the program to readily have
     * access to key infromation about the logged-in User
     * 
     * @return is the logged in User
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * doLoginCredsMatch will test if inputted username and password match what is
     * store on DB
     * 
     * @param username is the user inputted username
     * @param password is the user inputted password
     * @return is true if the credentials match and the user is good to login
     */
    private boolean doLoginCredsMatch(String username, String password) {

        // try to get matching credentials
        currentUser = userDao.getByUsername(username);

        // if user doesn't exists or if password doesn't match return false
        if (currentUser == null
                || !hashPassword(password, currentUser.getPasswordSalt()).equals(currentUser.getPasswordHash())) {
            currentUser = null;
            return false;
        }
        return true;
    }

    /**
     * hashPassword hashes passwords for security. Call when creating a user, and
     * logging in.
     * 
     * @param password is the user inputted password
     * @param salt     is the randomly generated salt
     * @return is the hashed password
     */
    private String hashPassword(String password, byte[] salt) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            password = Hex.encodeHexString(res);
            return password;

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (InvalidKeySpecException e) {
            System.out.println(e);
        }

        return null;
    }
}
