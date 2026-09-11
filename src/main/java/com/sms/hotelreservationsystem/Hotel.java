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
public class Hotel {
    private ArrayList<Room> rooms;
    private ArrayList<Guest> guests;
    
    public Hotel(){
        rooms = new ArrayList<>();
        guests = new ArrayList<>();
    
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
    public void addGuest(Guest guest){
        for(Guest g: guests){
            if(g.getGuestId() == guest.getGuestId()){
                return;
                
            
            }
        
        }
    
    }
    
    
}
