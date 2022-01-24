package com.fxcalc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnecton
{
    private Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Calc.sqlite");
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return connection;
    }

}
