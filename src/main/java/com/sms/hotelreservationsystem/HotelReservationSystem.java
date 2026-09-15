/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sms.hotelreservationsystem;




/**
 *
 * @author Riel
 */

import java.util.*;
import java.time.LocalDate;
public class HotelReservationSystem {

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Guest guest1 = new Guest(1, "Riel So", "0912312213", "rielso@gmail.com" );
        Room room1 = new Room(205, "DELUXE", 2500);
        hotel.addGuest(guest1);
        hotel.addRoom(room1);
        
        LocalDate checkIn = LocalDate.of(2026, 9, 27);
        LocalDate checkOut = LocalDate.of(2026, 9, 30);
        hotel.createReservation(1, 1, 205, checkIn, checkOut);
        
        System.out.println("\n---Try checking out before checking in-----\n");
        hotel.checkOutGuest(1);
        
        System.out.println("\n------Checking-in-----\n");
        hotel.checkInGuest(1);
        hotel.viewAllReservations();
        hotel.viewAllRooms();
        
        System.out.println("\n-----Trying to check in again-----\n");
        hotel.checkInGuest(1);
        
        System.out.println("\n----Check Out Properly----\n");
        hotel.checkOutGuest(1);
        hotel.viewAllReservations();
        hotel.viewAllRooms();
        
        System.out.println("\n----Trying to check out again-----\n");
        hotel.checkOutGuest(1);
        
        System.out.println("\n----Testing non-existent reservation----\n");
        hotel.checkInGuest(605);
        hotel.checkOutGuest(605);
        
        
        
    }
}