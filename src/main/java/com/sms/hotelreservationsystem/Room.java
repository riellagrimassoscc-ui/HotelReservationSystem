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

    /**
     * Database Insert Method
     * @param roomNumber
     * @param roomType
     * @param pricePerNight
     * @return true if the room was added successfully, false otherwise
     * @throws SQLException
     */
    public static boolean add(String roomNumber, String roomType, double pricePerNight) {
        String query = "INSERT INTO rooms (room_number, room_type, price_per_night) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Bind Parameters
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, roomType);
            pstmt.setDouble(3, pricePerNight);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
            return false;
        }
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
            System.out.println("Price cannot be negative");
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
