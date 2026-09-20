/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */
public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private RoomStatus status;
    
    
    //Constructor header
    public Room(int roomNumber, String roomType, double pricePerNight){
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }
    
    //Getters
    public int getRoomNumber(){
        return roomNumber;
    }
    public String getRoomType(){
        return roomType;
    }
    public double getPricePerNight(){
        return pricePerNight;
    }
    public RoomStatus getStatus(){
        return status;
    }
    
    //Setters
    public void setRoomType(String roomType){
        if(!roomType.trim().isEmpty()){
            this.roomType = roomType;
        }else{
            System.out.println("Invalid room type. Room type cannot be empty.");
        }
    }
    
    public void setPricePerNight(double pricePerNight){
        if(pricePerNight > 0){
            this.pricePerNight = pricePerNight;
        }else{
            System.out.println("Price must be greater than zero");
        }
    }
    
    public void setStatus(RoomStatus status){
        this.status = status;
    }
    
    public boolean isAvailable(){
        return status == RoomStatus.AVAILABLE;
    
    }
    
    @Override
    public String toString(){
            return "Room Number: " + roomNumber + "\n" +
                    "Room Type: " + roomType + "\n" +
                    "Price Per Night: " + pricePerNight + "\n" +
                    "Status: " + status + "\n";
    }
}
