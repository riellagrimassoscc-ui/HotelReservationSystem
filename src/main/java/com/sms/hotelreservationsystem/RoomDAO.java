/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomDAO {

    public static void saveRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setDouble(3, room.getPricePerNight());
            stmt.setString(4, room.getStatus().toString());

            stmt.executeUpdate();
            System.out.println("Room " + room.getRoomNumber() + " saved to database!");

        } catch (SQLException e) {
            System.out.println("Failed to save room!");
            e.printStackTrace();
        }

    }

    public static ArrayList<Room> findAllRooms() {
        String sql = "SELECT * from rooms";

        ArrayList<Room> rooms = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                int roomNumber = result.getInt("room_number");
                String roomType = result.getString("room_type");
                double price = result.getDouble("price_per_night");
                String status = result.getString("status");

                Room room = new Room(roomNumber, roomType, price);
                room.setStatus(RoomStatus.valueOf(status));

                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve rooms! ");
            e.printStackTrace();

        }

        return rooms;

    }

    public static Room findRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int number = result.getInt("room_number");
                String roomType = result.getString("room_type");
                double price = result.getDouble("price_per_night");
                String status = result.getString("status");

                Room room = new Room(number, roomType, price);
                room.setStatus(RoomStatus.valueOf(status));

                return room;

            }

        } catch (SQLException e) {
            System.out.println("Failed to find room!");
            e.printStackTrace();

        }
        return null;

    }

    public static ArrayList<Room> findRoomsByType(String roomType) {
        String sql = "SELECT * FROM rooms WHERE room_type = ?";

        ArrayList<Room> rooms = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, roomType);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                int roomNumber = result.getInt("room_number");
                String typeOfRoom = result.getString("room_type");
                double price = result.getDouble("price_per_night");
                String status = result.getString("status");

                Room room = new Room(roomNumber, typeOfRoom, price);
                room.setStatus(RoomStatus.valueOf(status));

                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println("Failed to find room!");
            e.printStackTrace();

        }
        return rooms;

    }
    public static void updateRoomStatus(int roomNumber, String status){
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, status);
            stmt.setInt(2, roomNumber);
            stmt.executeUpdate();
        
        
        }catch(SQLException e){
            System.out.println("Failed to update room status!");
            e.printStackTrace();
        
        
        }
        
    
    
    }

}
