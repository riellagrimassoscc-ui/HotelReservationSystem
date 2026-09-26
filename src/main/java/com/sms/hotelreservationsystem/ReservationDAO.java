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
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationDAO {

    public static boolean saveReservation(int reservationId,
            int guestId, int roomNumber, double totalPrice,
            double payment, LocalDate checkIn, LocalDate checkOut,
            String status) {

        String sql = "INSERT INTO reservations "
                + "(reservation_id, guest_id, room_number, check_in_date, check_out_date, total_price, payment, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            stmt.setInt(2, guestId);
            stmt.setInt(3, roomNumber);
            stmt.setDate(4, java.sql.Date.valueOf(checkIn));
            stmt.setDate(5, java.sql.Date.valueOf(checkOut));
            stmt.setDouble(6, totalPrice);
            stmt.setDouble(7, payment);
            stmt.setString(8, status);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to save reservation");
            e.printStackTrace();
            return false;
        }
    }
    public static int getNextReservationId(){
        String sql = "SELECT COALESCE(MAX(reservation_id), 0) + 1 AS next_id FROM reservations";
        
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                return result.getInt("next_id");
                
            
            
            }
        
        }catch(SQLException e){
            System.out.println("Failed to get next reservation ID");
            e.printStackTrace();
        
        
        }
        return 1;
    
    
    }

    public static Reservation findReservationById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                int id = result.getInt("reservation_id");
                int guestId = result.getInt("guest_id");
                int roomNumber = result.getInt("room_number");
                LocalDate checkIn = result.getDate("check_in_date").toLocalDate();
                LocalDate checkOut = result.getDate("check_out_date").toLocalDate();
                double totalPrice = result.getDouble("total_price");
                double payment = result.getDouble("payment");
                String status = result.getString("status");

                Guest guest = GuestDAO.findGuestById(guestId);
                Room room = RoomDAO.findRoomByNumber(roomNumber);
                if (guest == null || room == null) {
                    return null;

                }
                Reservation reservation = new Reservation(id, guest, room, checkIn, checkOut, totalPrice);

                reservation.setPayment(payment);
                reservation.setStatus(ReservationStatus.valueOf(status));
                return reservation;

            }

        } catch (SQLException e) {
            System.out.println("Failed to find reservation!");
            e.printStackTrace();

        }
        return null;

    }

    public static ArrayList<Reservation> findReservationsByGuestId(int guestId) {

        String sql = "SELECT * FROM reservations WHERE guest_id = ?";

        ArrayList<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, guestId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {

                int reservationId = result.getInt("reservation_id");
                int roomNumber = result.getInt("room_number");

                LocalDate checkIn = result.getDate("check_in_date").toLocalDate();
                LocalDate checkOut = result.getDate("check_out_date").toLocalDate();

                double totalPrice = result.getDouble("total_price");
                double payment = result.getDouble("payment");

                String status = result.getString("status");

                Guest guest = GuestDAO.findGuestById(guestId);
                Room room = RoomDAO.findRoomByNumber(roomNumber);

                if (guest != null && room != null) {

                    Reservation reservation = new Reservation(
                            reservationId,
                            guest,
                            room,
                            checkIn,
                            checkOut,
                            totalPrice
                    );

                    reservation.setPayment(payment);
                    reservation.setStatus(ReservationStatus.valueOf(status));

                    reservations.add(reservation);
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to find guest reservations!");
            e.printStackTrace();
        }

        return reservations;
    }
    public static boolean updateReservation(int reservationId,
            LocalDate checkIn, LocalDate checkOut,
            double totalPrice, double payment){
        
        
        String sql = "UPDATE reservations "
            + "SET check_in_date = ?, "
            + "check_out_date = ?, "
            + "total_price = ?, "
            + "payment = ? "
            + "WHERE reservation_id = ?";
        
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, java.sql.Date.valueOf(checkIn));
            stmt.setDate(2, java.sql.Date.valueOf(checkOut));
            stmt.setDouble(3, totalPrice);
            stmt.setDouble(4, payment);
            stmt.setInt(5, reservationId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        
        }catch(SQLException e){
            System.out.println("Failed to update reservation!");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean cancelReservation(int reservationId){
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, reservationId);
            
            int rowsDeleted = stmt.executeUpdate();
            
            return rowsDeleted > 0;
        
        
        }catch(SQLException e){
            System.out.println("Failed to cancel reservation!");
            e.printStackTrace();
            return false;
        
        
        }
    
    
    }
    public static boolean updateReservationStatus(int reservationId, String status){
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        
        try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        
        
        }catch(SQLException e){
            System.out.println("Failed to update reservation status!");
            e.printStackTrace();
            return false;
        
        
        }
    
    
    } 

}
