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
import java.time.Month;
public class HotelReservationSystem {

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Guest guest1 = new Guest(1,"Riel So", "091232131123","rielso@gmail.com");
        Room room1 = new Room(205,"DELUXE",2500);
        hotel.addGuest(guest1);
        hotel.addRoom(room1);
        
        LocalDate checkIn = LocalDate.of(2026, 9, 27);
        LocalDate checkOut = LocalDate.of(2026, 9, 30);
        hotel.createReservation(1, 1, 205, checkIn, checkOut);
        
        hotel.viewAllReservations();
        hotel.viewAllRooms();
        
        System.out.println("Trying another booking");
        hotel.createReservation(2, 1, 205, checkIn, checkOut);
        
        
        System.out.println("Trying with a non-existent guest");
        hotel.createReservation(3, 999, 205, checkIn, checkOut);
        System.out.println("Trying with a non-existent room");
        hotel.createReservation(3, 1, 609, checkIn, checkOut);
        
        System.out.println("Cancelling the reservation");
        hotel.cancelReservation(1);
        hotel.viewAllReservations();
        hotel.viewAllRooms();
        
        System.out.println("Cancelling a non-existent reservation");
        hotel.cancelReservation(609);
        
    }
}