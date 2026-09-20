/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseTest {
     public static void main(String[] args) {

        String url = "jdbc:derby:HotelDB;create=true";

        try {
            Connection connection = DriverManager.getConnection(url);

            System.out.println("Database connected successfully!");

            connection.close();

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
    
}
