/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class Hotel {
    private ArrayList<Room> rooms;
    private ArrayList<Guest> guests;
    private ArrayList<Reservation> reservations;
    
    public Hotel(){
        rooms = new ArrayList<>();
        guests = new ArrayList<>();
        reservations = new ArrayList<>();
    
    }
   
    public void addRoom(Room room){
        Room existingRoom = findRoomByNumber(room.getRoomNumber());
        if(existingRoom != null){
            System.out.println("Room already exists");
            System.out.println("********************");
            return;
        }
        
        rooms.add(room);
    }
    
    public void removeRoom(int roomNumber){
        for(Room r : rooms ){
            if(r.getRoomNumber() == roomNumber){
                rooms.remove(r);
                break;
            }
        }
    }
    
    public void viewAllRooms(){
        for(Room r : rooms){
            System.out.println(r);
        }
    }
    
    public void viewAvailableRooms(){
        for(Room r : rooms){
            if(r.isAvailable()){
                System.out.println(r);
            }    
        }
    }
    
    
    public Room findRoomByNumber(int roomNumber){
        for(Room r : rooms){
            if(r.getRoomNumber() == roomNumber){
                return r;
            }
        }
        return null;
    }
    
    public ArrayList<Room> findRoomsByType(String roomType){
        ArrayList<Room> matches = new ArrayList();
        for(Room r : rooms){
            if(r.getRoomType().equals(roomType)){
                matches.add(r);
            }
        }
        return matches;   
    }
 
    public void changeRoomStatus(int roomNumber, RoomStatus newStatus){
        Room room = findRoomByNumber(roomNumber);
        if(room == null){
            System.out.println("Room not found");
            return;
        }
        room.setStatus(newStatus);
    }
    
    public void updateRoomPrice(int roomNumber, double newPrice){
        Room room = findRoomByNumber(roomNumber);
        if(room == null){
            System.out.println("Room not found");
            return;
        }
        room.setPricePerNight(newPrice);
    }
    
    
    //For guest
    public void addGuest(Guest guest){
        for(Guest g: guests){
            if(g.getGuestId() == guest.getGuestId()){
                System.out.println("Guest ID already exists");
                return;
            }
        }
         guests.add(guest);
    }
    
    public void viewAllGuests(){
        for(Guest g: guests){
            System.out.println(g);    
        }
    }
    
    public Guest findGuestById(int guestId){
        for(Guest g: guests){
            if(g.getGuestId() == guestId){
                return g;
            }    
        }
        return null;
    }
    
    
    public Guest findGuestByEmail(String email){
        for(Guest e: guests){
            if(e.getEmail().equals(email)){
                return e;
            }
        }
        return null;
    }
    public void updateGuestName(int guestId, String newName){
        Guest guest = findGuestById(guestId);
        if(guest == null){
            System.out.println("Guest's name not found!");
            return;
        }
    guest.setName(newName);
    }
    public void updateGuestPhone(int guestId, String newPhone){
        Guest guest = findGuestById(guestId);
        if(guest == null){
            System.out.println("Guest's phone not found!");
            return;
        
        }
    guest.setPhone(newPhone);
    }
    public void updateGuestEmail(int guestId, String newEmail){
        Guest guest = findGuestById(guestId);
        if(guest == null){
            System.out.println("Guest's email not found!");
            return;
        
        }
    guest.setEmail(newEmail);
    }
    
    
    public boolean checkRoomAvailability(int roomNumber){
        Room room = findRoomByNumber(roomNumber);
        if(room == null){
            return false;
        }
        return room.isAvailable();
    }
    
   public void createReservation(int reservationId,
           int guestId,
           int roomNumber,
           LocalDate checkIn,
           LocalDate checkOut){
       Guest guest = findGuestById(guestId);
       if(guest == null){
           System.out.println("Guest not found");
           return;
       
       }
       Room room = findRoomByNumber(roomNumber);
       if(room == null){
           System.out.println("Room not found");
           return;
       }
       if(!room.isAvailable()){
           System.out.println("Room is not avaiable");
           return;
       
       }
       long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
       double totalPrice = nights * room.getPricePerNight();
       
       Reservation newReservation = new Reservation(reservationId,
               guest, room, 
               checkIn, checkOut, 
               totalPrice);
       
       reservations.add(newReservation);
       room.setStatus(RoomStatus.RESERVED);
   
   }
   public void viewAllReservations(){
       for(Reservation r: reservations){
           System.out.println(r);
       }
   }
   public Reservation findReservationById(int reservationId){
        for(Reservation r: reservations){
            if(r.getReservationId() == reservationId){
                return r;
            }    
        }
        return null;
    }
   public void cancelReservation(int reservationId){
       Reservation reservation = findReservationById(reservationId);
       if(reservation == null){
           System.out.println("Reservation ID not found!");
           return;
       
       }
       reservation.getRoom().setStatus(RoomStatus.AVAILABLE);
       reservation.setStatus(ReservationStatus.CANCELLED);
   }
   public void checkInGuest(int reservationId){
       Reservation reservation = findReservationById(reservationId);
       if(reservation == null){
           System.out.println("Reservation ID not found!");
           return;
       }
       if(reservation.getStatus() == ReservationStatus.CONFIRMED){
           reservation.getRoom().setStatus(RoomStatus.OCCUPIED);
           reservation.setStatus(ReservationStatus.CHECKED_IN);
       }else{
           System.out.println("Cannot check in. Reservation status is: " + reservation.getStatus());
       
       }
    }
   public void checkOutGuest(int reservationId){
       Reservation reservation = findReservationById(reservationId);
       if(reservation == null){
           System.out.println("Reservation ID not found!");
           return;
       }
       if(reservation.getStatus() == ReservationStatus.CHECKED_IN){
           reservation.getRoom().setStatus(RoomStatus.AVAILABLE);
           reservation.setStatus(ReservationStatus.CHECKED_OUT);
       
       }else{
           System.out.println("Cannot check out. Reservation status is: " + reservation.getStatus());
       }
   }
}
