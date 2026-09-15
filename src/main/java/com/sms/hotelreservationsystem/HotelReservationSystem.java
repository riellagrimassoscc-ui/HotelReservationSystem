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
        Guest guest1 = new Guest(1,"Riel So", "091232131123","rielso@gmail.com");
        Room room1 = new Room(205,"DELUXE",2500);
        
        LocalDate checkIn = LocalDate.of(2026, 9, 27);
        LocalDate checkOut = LocalDate.of(2026, 9, 30);
        Reservation res1 = new Reservation(1, guest1, room1, checkIn, checkOut, 7500);
        
        System.out.println(res1);
    }
}