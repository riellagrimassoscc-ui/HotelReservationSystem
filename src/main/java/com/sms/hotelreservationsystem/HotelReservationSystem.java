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
            
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Room Management");
            System.out.println("2. Guest Management");
            System.out.println("3. Reservation Management");
            System.out.println("4. Check-in / Check-out");
            System.out.println("5. Exit");
            System.out.print("Enter choice(1-5 only): ");
            
            int choice = 0;
            if(scanner.hasNextInt()){
               choice = scanner.nextInt();
            }else{
                System.out.println("Invalid input! Enter a number only!");
                scanner.next();
            }
            
            switch(choice){
                case 1:
                    roomManagementMenu();
                    break;
                case 2:
                    guestManagementMenu();
                    break;
                case 3:
                    reservationManagementMenu();
                    break;
                case 4:
                    checkInOutMenu();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using the reservation");
                    break;
                default:
                    System.out.println("Invalid input! Choose 1-5 only!");
            }
        }while(running);
    }
    
    public static void roomManagementMenu(){
        boolean back = false;
        
        do{
            System.out.println("\n--- Room Management ---");
            System.out.println("1. Add Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. View Available Rooms");
            System.out.println("4. Search Room by Number");
            System.out.println("5. Search Rooms by Type");
            System.out.println("6. Update Room Price");
            System.out.println("7. Remove Room");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter choice(1-8): ");
            int choice = 0;
            if(scanner.hasNextInt()){
                choice = scanner.nextInt();
            }else{
                System.out.println("Invalid input! Try number only");
            }
            switch(choice){
                case 1:
                    addRoom();
                    break;
                case 2:
                    hotel.viewAllRooms();
                    break;
                case 3:
                    hotel.viewAvailableRooms();
                    break;
                case 4:
                    searchRoomByNumber();
                    break;
                case 5:
                    searchRoomsByType();
                    break;
                case 6:
                    updateRoomPriceMenu();
                    break;
                case 7:
                    removeRoomMenu();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid input! Choose 1-8 only");
            }
        }while(!back);
    }
    
    public static void addRoom(){
        int roomNumber = 0;
        System.out.print("Enter room number: ");
        if(scanner.hasNextInt()){
            roomNumber = scanner.nextInt();
        }else{
            System.out.println("Invalid input. Enter a number only!");
            scanner.next();
        }
        
        
        int choice = 0;
        String roomType = "";
        double price = 0;
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
        }
        
        
        
        switch(choice){
            case 1:
                roomType = "SINGLE";
                price = 1500;
                validType = true;
                break;
            case 2:
                roomType = "DELUXE";
                price = 2500;
                validType = true;
                break;
            case 3:
                roomType = "PREMIUM";
                price = 3500;
                validType = true;
                break;
            case 4:
                roomType = "SUITE";
                price = 4500;
                validType = true;
                break;
            default:
                System.out.println("Invalid input. Choose 1-4 only!");
        }
        
       
        }while(!validType);
        Room room = new Room(roomNumber, roomType, price);
        hotel.addRoom(room);
        
    }
    public static void createReservationMenu(){
       int choice = 0;
       String roomType = "";
       double price = 0;
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
             System.out.println("Enter choice: ");
             choice = scanner.nextInt();
             
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
    public static void updateRoomPriceMenu(){
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
public static void removeRoomMenu(){}
public static void guestManagementMenu(){}
public static void reservationManagementMenu(){}
public static void checkInOutMenu(){}
}