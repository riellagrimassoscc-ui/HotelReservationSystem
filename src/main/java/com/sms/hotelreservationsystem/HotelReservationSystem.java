/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sms.hotelreservationsystem;




/**
 *
 * @author Riel
 */

import java.util.*;
public class HotelReservationSystem {

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        
        Guest guest1 = new Guest (1, "Riel So", " 0912321321", "@gmail.com");
        Guest guest2 = new Guest (2, "Yel", "09123123", "yel@gmail.com");
        Guest guest3 = new Guest (1, "Aggin", "0912311111", "@yahoo.com");
        hotel.addGuest(guest1);
        System.out.println();
        hotel.addGuest(guest2);
        System.out.println();
        hotel.addGuest(guest3);
        System.out.println();
        
        
        Guest foundGuest = hotel.findGuestById(2);
        System.out.println(foundGuest);
        Guest foundGuestEmail = hotel.findGuestByEmail("yel@gmail.com");
        System.out.println(foundGuestEmail);
        Guest notFoundGuestEmail = hotel.findGuestByEmail("nonexistent@gmail.com");
        System.out.println(notFoundGuestEmail);
        Guest notFoundGuest = hotel.findGuestById(10);
        System.out.println(notFoundGuest);
        
        hotel.updateGuestName(1, "Setsuna F. Seie");
        hotel.updateGuestPhone(1, "098765321");
        hotel.updateGuestEmail(1,"niceone@gmail.com");
        hotel.viewAllGuests();
    }
}