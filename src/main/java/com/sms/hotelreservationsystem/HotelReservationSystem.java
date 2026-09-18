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
    static Hotel hotel = new Hotel();
    static Scanner scanner = new Scanner(System.in);
    static int nextReservationId = 1;
    static int nextGuestId = 1;
   
    
  
    public static void main(String[] args) {
        for(int i = 101; i <= 110; i++){
            hotel.addRoom(new Room(i, "SINGLE", 1500));
        
        }
        for(int i = 201; i <= 210; i++){
            hotel.addRoom(new Room (i, "DELUXE", 2500));
        
        }
        for(int i = 301; i <= 310; i++){
            hotel.addRoom(new Room(i, "PREMIUM", 3500));
        }
        for(int i = 401; i <= 410; i++){
            hotel.addRoom(new Room(i, "SUITE", 4500));
        }
        
        
        
        boolean running = true;
        
        
        do{
            
            System.out.println("\n===== HOTEL RESERVATION =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Search Room By Number");
            System.out.println("3. Search Rooms By Type");
            System.out.println("4. Make Reservation");
            System.out.println("5. Reservation Management");
            System.out.println("6. Check-in / Check-out ");
            System.out.println("7. Exit ");
            int choice = 0;
            if(scanner.hasNextInt()){
               choice = scanner.nextInt();
            }else{
                System.out.println("Invalid input! Enter a number only!");
                scanner.next();
            }
            
            switch(choice){
                case 1:
                    hotel.viewAvailableRooms();
                    break;
                case 2:
                    searchRoomByNumber();
                    break;
                case 3:
                    searchRoomsByType();
                    break;
                case 4:
                    createReservationMenu();
                    break;
                case 5:
                    reservationManagementMenu();
                    break;
                case 6:
                    checkInOutMenu();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using the reservation system");
                    break;
                default:
                    System.out.println("Invalid input! Choose 1-7 only!");
            }
        }while(running);
    }
    public static void createReservationMenu(){
       int choice = 0;
       String roomType = "";
       boolean validType = false;
       
       do{
           System.out.println("Select room type:");
    System.out.println("1. SINGLE - PHP1500/NIGHT");
    System.out.println("2. DELUXE - PHP2500/NIGHT");
    System.out.println("3. PREMIUM - PHP3500/NIGHT");
    System.out.println("4. SUITE - PHP4500/NIGHT");
    System.out.print("Enter choice(1-4): ");
    
    if(scanner.hasNextInt()){
        choice = scanner.nextInt();
    }else{
        System.out.println("Invalid input. Enter a number only!");
        scanner.next();
    }
    
    switch(choice){
        case 1: roomType = "SINGLE"; validType = true; break;
        case 2: roomType = "DELUXE"; validType = true; break;
        case 3: roomType = "PREMIUM"; validType = true; break;
        case 4: roomType = "SUITE"; validType = true; break;
        default: System.out.println("Invalid input. Choose 1-4 only!");
    }
       }while(!validType);
       scanner.nextLine();
       ArrayList<Room> matches = hotel.findRoomsByType(roomType);
       
       Room assignedRoom = null;
       for(Room r: matches){
           if(r.isAvailable()){
           assignedRoom = r;
           break;
        }
      }
       if(assignedRoom == null){
           System.out.println("No rooms of that type are currently available");
           return;
        }
       int reservationId = nextReservationId;
       nextReservationId++;
       
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        
        Guest guest = hotel.findGuestByEmail(email);
        
        if(guest == null){
            System.out.println("No email found. Lets create one!");
            int guestId = nextGuestId;
            nextGuestId++;
            
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            
            guest = new Guest(guestId, name, phone, email);
            hotel.addGuest(guest);
            
           
        }
            System.out.print("Enter check-in date(yyyy-MM-dd): ");
            String inputCheckIn = scanner.nextLine();
            LocalDate checkIn = LocalDate.parse(inputCheckIn);
            
            System.out.print("Enter check-out date(yyyy-MM-dd): ");
            String inputCheckOut = scanner.nextLine();
            LocalDate checkOut = LocalDate.parse(inputCheckOut);
            hotel.createReservation(reservationId,
                    guest.getGuestId(), 
                    assignedRoom.getRoomNumber(),
                    checkIn, checkOut);
    }
    
    
    public static void searchRoomByNumber(){
        int roomNumber = 0;
        System.out.print("Enter room number: ");
        if(scanner.hasNextInt()){
            roomNumber = scanner.nextInt();
            Room room = hotel.findRoomByNumber(roomNumber);
            if(room == null){
                System.out.println("Room not found!");
            }else{
                System.out.println(room);
            }
        }else{
            System.out.println("Enter a number only");
            scanner.next();
        }
    }
    public static void searchRoomsByType(){
        String roomType = "";
        int choice = 0;
        boolean validType = false; 
        
        do{
             System.out.println("Choose Room Type");
             System.out.println("1. SINGLE\n2. DELUXE\n3. PREMIUM\n4. SUITE");
             System.out.println("Enter choice(1-4 only): ");
             if(scanner.hasNextInt()){
                 choice = scanner.nextInt();
             }else{
                 System.out.println("Enter a number only!");
                 scanner.next();
             }
             
             switch(choice){
                case 1: 
                    roomType = "SINGLE"; 
                    validType = true; 
                    break;
                case 2: 
                    roomType = "DELUXE";
                    validType = true; 
                    break;
                case 3: 
                    roomType = "PREMIUM";
                    validType = true;
                    break;
                case 4: 
                    roomType = "SUITE";
                    validType = true; 
                    break;
                default:
                    System.out.println("Invalid input. Choose 1-4 only!");
             }
        }while(!validType);
        ArrayList<Room> matches = hotel.findRoomsByType(roomType);
        if(matches.isEmpty()){
            System.out.println("No rooms found");
        }else{
            for(Room r: matches){
            System.out.println(r);
        
        }
        
        }
        
    }
public static void reservationManagementMenu(){}
public static void checkInOutMenu(){}
}