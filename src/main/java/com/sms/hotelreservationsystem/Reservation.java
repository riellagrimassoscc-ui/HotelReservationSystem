/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sms.hotelreservationsystem;

/**
 *
 * @author Riel
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private int reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalPrice;
    private double payment;
    private ReservationStatus status;

    public Reservation(int reservationId,
            Guest guest,
            Room room,
            LocalDate checkIn,
            LocalDate checkOut,
            double totalPrice) {

        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.payment = totalPrice;
        this.status = ReservationStatus.CONFIRMED;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPayment() {
        return payment;

    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setCheckIn(LocalDate checkIn) {
        if (checkIn != null && checkIn.isBefore(checkOut)) {
            this.checkIn = checkIn;
        } else {
            System.out.println("Invalid check-in date.");
        }
    }

    public void setCheckOut(LocalDate checkOut) {
        if (checkOut != null && checkOut.isAfter(checkIn)) {
            this.checkOut = checkOut;
        } else {
            System.out.println("Check-out date must be after check-in date.");
        }
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice > 0) {
            this.totalPrice = totalPrice;
        } else {
            System.out.println("Total price cannot be negative");
        }
    }

    public void setPayment(double payment) {
        if (payment >= 0) {
            this.payment = payment;

        } else {
            System.out.println("Payment cannot be negative");

        }

    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public boolean updateDates(LocalDate newCheckIn, LocalDate newCheckOut) {
        if (newCheckIn != null && newCheckOut != null && newCheckIn.isBefore(newCheckOut)) {
            this.checkIn = newCheckIn;
            this.checkOut = newCheckOut;
            return true;

        } else {
            System.out.println("Invalid reservation date");
            return false;

        }
    }

    public void updateTotalPrice() {
        this.totalPrice = getNumberOfNights() * room.getPricePerNight();

    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + "\n"
                + "Guest: " + guest.getName() + "\n"
                + "Room: " + room.getRoomNumber() + " (" + room.getRoomType() + ")" + "\n"
                + "Check-in: " + checkIn + "\n"
                + "Check-out: " + checkOut + "\n"
                + "Total Price: " + totalPrice + "\n"
                + "Payment: " + payment + "\n"
                + "Status: " + status + "\n";
    }
}
