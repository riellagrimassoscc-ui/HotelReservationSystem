/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sms.hotelreservationsystem;




/**
 *
 * @author Riel
 */

import java.sql.Connection;

public class HotelReservationSystem {

    public static void main(String[] args) {
        System.out.println("Starting Hotel Reservation System...");

        DatabaseConnection.initializeDatabase();
        boolean isSuccess = Room.add("101", "Deluxe Suite", 2500.00);
        if (isSuccess) {
            System.out.println("Room 101 successfully added to database!");
        } else {
            System.out.println("Room 101 already exists or failed to insert.");
        }

        // Paggamit ng modular DatabaseConnection class
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to SQLite successfully via DatabaseConnection class!");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}