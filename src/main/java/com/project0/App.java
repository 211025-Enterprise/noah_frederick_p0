package com.project0;

import com.sql.SQL;
import java.util.List;

public final class App {
    public static void main(String[] args) throws Exception {

        SQL sql = new SQL();

        // print all usernames
        System.out.println("All Current Users:");
        for (String i : sql.getUserList()) {
            System.out.println(i);
        }

    }
}
