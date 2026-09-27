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
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestDAO {

    public static int saveGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection(); 
                PreparedStatement stmt = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {
            
            
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getEmail());
            stmt.setString(3, guest.getPhone());
            int rowsInserted = stmt.executeUpdate();
            
            if(rowsInserted > 0){
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                
                }
            
            
            }

        } catch (SQLException e) {
            System.out.println("Failed to save guest");
            e.printStackTrace();
            

        }
        return -1;

    }

    public static Guest findGuestByEmail(String email) {
        String sql = "SELECT * FROM guests where email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                int guestId = result.getInt("guest_id");
                String name = result.getString("name");
                String phone = result.getString("phone");
                String guestEmail = result.getString("email");
                
                Guest guest = new Guest(guestId, name, phone, guestEmail);
                return guest;
            
            }

        } catch (SQLException e) {
            System.out.println("Failed to find guest");
            e.printStackTrace();

        }
        return null;

    }
    public static Guest findGuestById(int guestId) {

    String sql = "SELECT * FROM guests WHERE guest_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setInt(1, guestId);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {

            int id = result.getInt("guest_id");
            String name = result.getString("name");
            String phone = result.getString("phone");
            String email = result.getString("email");

            return new Guest(id, name, phone, email);
        }

    } catch (SQLException e) {
        System.out.println("Failed to find guest!");
        e.printStackTrace();
    }

    return null;
}
   
}