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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.io.IOException;

public class HotelReservationSystem {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        do {
            int choice = displayMenu();

            switch (choice) {
                case 1:
                    viewAvailableRoomsFromDatabase();
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
                    checkInMenu();
                    break;

                case 7:
                    checkOutMenu();
                    break;

                case 8:
                    running = false;
                    System.out.println("Thank you for using the reservation system");
                    break;

                default:
                    System.out.println("Invalid input! Choose 1-8 only!");
            }

        } while (running);

    }

    public static int displayMenu() {
        clearScreen();

        System.out.println("========== HOTEL RESERVATION SYSTEM ==========\n");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Search Room By Number");
        System.out.println("3. Search Rooms By Type");
        System.out.println("4. Make Reservation");
        System.out.println("5. Reservation Management");
        System.out.println("6. Check In");
        System.out.println("7. Check Out");
        System.out.println("8. Exit");
        System.out.println( "\n===============================================" );

        return readInt("Enter choice(1-8 only): ");
    }

    public static void createReservationMenu() {
        int choice = 0;
        String roomType = "";
        boolean validType = false;

        do {
            System.out.println("Select room type:");
            System.out.println("""
               1. SINGLE - PHP1500/NIGHT
               2. DELUXE - PHP2500/NIGHT
               3. PREMIUM - PHP3500/NIGHT
               4. SUITE - PHP4500/NIGHT""");
            choice = readInt("Enter choice(1-4 only): ");

            switch (choice) {
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

        } while (!validType);

        ArrayList<Room> matches = RoomDAO.findRoomsByType(roomType);

        Room assignedRoom = null;

        for (Room r : matches) {
            if (r.isAvailable()) {
                assignedRoom = r;
                break;
            }
        }

        if (assignedRoom == null) {
            System.out.println("No rooms of that type are currently available");
            pauseScreen();
            return;
        }

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = GuestDAO.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("No email found. Lets create one!");

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();

            guest = new Guest(0, name, phone, email);
            int guestId = GuestDAO.saveGuest(guest);

            if (guestId == -1) {
                System.out.println("Guest was not saved. Try again.");
                pauseScreen();
                return;

            }
            guest = new Guest(guestId, name, phone, email);

        }

        LocalDate checkIn = null;
        LocalDate checkOut = null;
        boolean validDates = false;

        do {
            System.out.print("Enter check-in date(yyyy-MM-dd): ");
            String inputCheckIn = scanner.nextLine();

            System.out.print("Enter check-out date(yyyy-MM-dd): ");
            String inputCheckOut = scanner.nextLine();

            try {
                checkIn = LocalDate.parse(inputCheckIn);
                checkOut = LocalDate.parse(inputCheckOut);

                if (checkIn.isBefore(checkOut)) {
                    validDates = true;
                } else {
                    System.out.println("Invalid date! Check-out must be after check-in.");
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Use yyyy-MM-dd and enter a real date.");
            }

        } while (!validDates);

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = nights * assignedRoom.getPricePerNight();

        System.out.println("\n========== RESERVATION SUMMARY ==========");
        System.out.println("Guest: " + guest.getName());
        System.out.println("Email: " + guest.getEmail());
        System.out.println("Phone: " + guest.getPhone());
        System.out.println("Room Number: " + assignedRoom.getRoomNumber());
        System.out.println("Room Type: " + assignedRoom.getRoomType());
        System.out.println("Price Per Night: PHP " + assignedRoom.getPricePerNight());
        System.out.println("Check-in: " + checkIn);
        System.out.println("Check-out: " + checkOut);
        System.out.println("Number of Nights: " + nights);
        System.out.println("Total Price: PHP " + totalPrice);
        System.out.println("==========================================");

        double payment;
        boolean validPayment = false;

        do {

            payment = readDouble("Enter payment: PHP ");

            if (payment <= 0) {

                System.out.println(
                        "Invalid payment! Payment must be greater than 0."
                );

            } else if (payment >= totalPrice) {

                double change = payment - totalPrice;

                int reservationId = ReservationDAO.saveReservation(
                        guest.getGuestId(),
                        assignedRoom.getRoomNumber(),
                        totalPrice,
                        payment,
                        checkIn,
                        checkOut,
                        "CONFIRMED"
                );

                if (reservationId > 0) {

                    boolean roomUpdated = RoomDAO.updateRoomStatus(
                            assignedRoom.getRoomNumber(),
                            "RESERVED"
                    );
                    if (roomUpdated) {

                        Reservation reservation
                                = ReservationDAO.findReservationById(reservationId);

                        reservationReceipt(
                                reservation,
                                payment,
                                change
                        );

                        pauseScreen();
                        validPayment = true;

                    } else {
                        System.out.println(
                                "Room status could not be updated. Reservation was cancelled.");
                        ReservationDAO.cancelReservation(reservationId);
                        pauseScreen();
                        return;

                    }
                } else {

                    System.out.println(
                            "Reservation was not saved. Please try again!"
                    );
                    pauseScreen();
                    return;
                }

            } else {

                System.out.println(
                        "Insufficient payment. Please enter enough payment"
                );
            }

        } while (!validPayment);
    }

    public static void searchRoomByNumber() {
        int roomNumber = readInt("Enter room number: ");

        clearScreen();

        Room room = RoomDAO.findRoomByNumber(roomNumber);

        if (room == null) {
            System.out.println("Room not found!");
        } else {
            System.out.println("===== ROOM INFORMATION =====\n");

            System.out.printf(
                    "%-12s %-12s %-15s %-12s%n",
                    "Room No.",
                    "Type",
                    "Price/Night",
                    "Status"
            );

            System.out.println("-----------------------------------------------");

            System.out.printf(
                    "%-12d %-12s PHP %-11.2f %-12s%n",
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPricePerNight(),
                    room.getStatus()
            );
        }

        pauseScreen();
    }

    public static void searchRoomsByType() {
        String roomType = "";
        int choice = 0;
        boolean validType = false;

        do {
            clearScreen();

            System.out.println("Choose Room Type");
            System.out.println("""
                           1. SINGLE
                           2. DELUXE
                           3. PREMIUM
                           4. SUITE""");

            choice = readInt("Enter choice (1-4 only): ");

            switch (choice) {
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
                    pauseScreen();
            }

        } while (!validType);

        clearScreen();

        ArrayList<Room> matches = RoomDAO.findRoomsByType(roomType);

        if (matches.isEmpty()) {
            System.out.println("No rooms found");
        } else {
            System.out.println("===== " + roomType + " ROOMS =====\n");

            System.out.printf(
                    "%-12s %-12s %-15s %-12s%n",
                    "Room No.",
                    "Type",
                    "Price/Night",
                    "Status"
            );

            System.out.println("-----------------------------------------------");

            for (Room room : matches) {
                System.out.printf(
                        "%-12d %-12s PHP %-11.2f %-12s%n",
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getPricePerNight(),
                        room.getStatus()
                );
            }
        }

        pauseScreen();
    }

    public static void reservationManagementMenu() {
        int choice = 0;
        boolean running = true;

        do {
            clearScreen();

            System.out.println("========== RESERVATION MANAGEMENT ==========\n");
            System.out.println("""
                               1. View my reservation
                               2. Update my reservation
                               3. Cancel my reservation
                               4. Back""");
            System.out.println( "\n=============================================");
            choice = readInt("Enter choice (1-4 only): ");

            switch (choice) {
                case 1:
                    viewMyReservations();
                    break;
                case 2:
                    updateMyReservation();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input! Choose 1-4 only");
                    pauseScreen();
            }

        } while (running);

    }

    public static void viewMyReservations() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        Guest guest = GuestDAO.findGuestByEmail(email);
        if (guest == null) {
            System.out.println("Guest not found");
            pauseScreen();
            return;
        }
        ArrayList<Reservation> matches = ReservationDAO.findReservationsByGuestId(guest.getGuestId());
        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            pauseScreen();
            return;
        }
        clearScreen();
        System.out.println("========== MY RESERVATIONS ==========\n");
        System.out.printf("%-5s %-8s %-12s %-15s %-15s %-18s %-15s%n", 
                "ID", "Room", "Type", "Check-in", "Check-out", "Total Price", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (Reservation r : matches) {
            System.out.printf("%-5d %-8d %-12s %-15s %-15s PHP %,-11.2f %-15s%n",
                    r.getReservationId(),
                    r.getRoom().getRoomNumber(),
                    r.getRoom().getRoomType(),
                    r.getCheckIn(),
                    r.getCheckOut(), 
                    r.getTotalPrice(),
                    r.getStatus());
        }
        System.out.println("\n================================================================================");
        pauseScreen();
    }

    public static void updateMyReservation() {
        int reservationId;

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = GuestDAO.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            pauseScreen();
            return;
        }

        ArrayList<Reservation> matches = ReservationDAO.findReservationsByGuestId(guest.getGuestId());

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            pauseScreen();
            return;
        }
        
        clearScreen();
        
        System.out.println("========== MY RESERVATIONS ==========\n");
        System.out.printf( "%-5s %-8s %-12s %-15s %-15s %-18s %-15s%n",
                "ID", "Room", "Type", "Check-in", "Check-out", "Total Price", "Status" );
        
        System.out.println( "--------------------------------------------------------------------------------" );
        

        for (Reservation r : matches) {
           System.out.printf( "%-5d %-8d %-12s %-15s %-15s PHP %,-11.2f %-15s%n", 
                   r.getReservationId(), 
                   r.getRoom().getRoomNumber(), 
                   r.getRoom().getRoomType(), 
                   r.getCheckIn(), 
                   r.getCheckOut(), 
                   r.getTotalPrice(), 
                   r.getStatus() );
        }
        System.out.println(
                "\n================================================================================" );

        reservationId = readInt("Enter reservation ID to update: ");

        Reservation reservation = ReservationDAO.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            pauseScreen();
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation doesnt belong to you");
            pauseScreen();
            return;
        }
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            System.out.println("Cannot update. Reservation has already been checked out.");
            pauseScreen();
            return;
        }

        LocalDate newCheckIn;
        LocalDate newCheckOut;

        try {

            if (reservation.getStatus() == ReservationStatus.CHECKED_IN) {
                newCheckIn = reservation.getCheckIn();

                System.out.println("Current check-in date: " + newCheckIn);
                System.out.print("Enter new check out (yyyy-MM-dd): ");
                String inputCheckOut = scanner.nextLine();

                newCheckOut = LocalDate.parse(inputCheckOut);

            } else {
                System.out.print("Enter new check in (yyyy-MM-dd): ");
                String inputCheckIn = scanner.nextLine();
                System.out.print("Enter new check out (yyyy-MM-dd): ");
                String inputCheckOut = scanner.nextLine();

                newCheckIn = LocalDate.parse(inputCheckIn);
                newCheckOut = LocalDate.parse(inputCheckOut);
            }

            if (!newCheckIn.isBefore(newCheckOut)) {
                System.out.println("Invalid date! Check-out must be after check-in.");
                pauseScreen();
                return;
            }

            long newNights = ChronoUnit.DAYS.between(
                    newCheckIn,
                    newCheckOut
            );

            double newTotalPrice
                    = newNights * reservation.getRoom().getPricePerNight();

            handlePaymentAdjustment(
                    reservation,
                    newCheckIn,
                    newCheckOut,
                    newTotalPrice
            );

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Invalid date! Use yyyy-MM-dd and enter a real date"
            );
            pauseScreen();
        }
    }

    public static void handlePaymentAdjustment(
            Reservation reservation,
            LocalDate newCheckIn,
            LocalDate newCheckOut,
            double newTotalPrice) {

        double paymentDifference = newTotalPrice - reservation.getPayment();

        if (paymentDifference > 0) {

            System.out.printf("Additional payment required: PHP %,.2f%n", paymentDifference);

            double payment = readDouble(
                    "Enter additional payment: PHP "
            );

            if (payment >= paymentDifference) {

                double change = payment - paymentDifference;

                double newPayment
                        = reservation.getPayment() + paymentDifference;

                boolean updated = ReservationDAO.updateReservation(
                        reservation.getReservationId(),
                        newCheckIn,
                        newCheckOut,
                        newTotalPrice,
                        newPayment
                );

                if (updated) {

                    System.out.println("Additional payment accepted");
                    System.out.printf("Change: PHP %,.2f%n", change);

                    reservation.updateDates(newCheckIn, newCheckOut);
                    reservation.updateTotalPrice();
                    reservation.setPayment(newPayment);

                    clearScreen();

                    System.out.println("Reservation updated successfully!");
                    System.out.println(reservation);
                    pauseScreen();

                } else {

                    System.out.println(
                            "Failed to update reservation in database"
                    );
                    pauseScreen();
                }

            } else {

                System.out.println(
                        "Insufficient additional payment"
                );
                pauseScreen();
            }

        } else if (paymentDifference == 0) {

            System.out.println(
                    "No additional payment required"
            );

            boolean updated = ReservationDAO.updateReservation(
                    reservation.getReservationId(),
                    newCheckIn,
                    newCheckOut,
                    newTotalPrice,
                    reservation.getPayment()
            );

            if (updated) {

                reservation.updateDates(newCheckIn, newCheckOut);
                reservation.updateTotalPrice();

                clearScreen();

                System.out.println(
                        "Reservation updated successfully!"
                );
                System.out.println(reservation);
                pauseScreen();

            } else {

                System.out.println(
                        "Failed to update reservation in database"
                );
                pauseScreen();
            }

        } else {

            double refund
                    = reservation.getPayment() - newTotalPrice;

            double newPayment = newTotalPrice;

            System.out.println(
                    "Reservation total decreased."
            );

            System.out.printf("Refund amount: PHP %,.2f%n", refund);

            boolean updated = ReservationDAO.updateReservation(
                    reservation.getReservationId(),
                    newCheckIn,
                    newCheckOut,
                    newTotalPrice,
                    newPayment
            );

            if (updated) {

                reservation.updateDates(newCheckIn, newCheckOut);
                reservation.updateTotalPrice();
                reservation.setPayment(newPayment);

                clearScreen();

                System.out.println(
                        "Reservation updated successfully!"
                );
                System.out.println(reservation);

                pauseScreen();

            } else {

                System.out.println(
                        "Failed to update reservation in database"
                );
                pauseScreen();
            }
        }
    }

    public static void cancelReservation() {
        int reservationId;

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = GuestDAO.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            pauseScreen();
            return;
        }

        ArrayList<Reservation> matches
                = ReservationDAO.findReservationsByGuestId(guest.getGuestId());

        if (matches.isEmpty()) {
            System.out.println("No reservation found");
            pauseScreen();
            return;
        }
        
        clearScreen();
        
        System.out.println("========== MY RESERVATIONS ==========\n");
        
        System.out.printf( "%-5s %-8s %-12s %-15s %-15s %-18s %-15s%n",
                "ID", "Room", "Type", "Check-in", "Check-out", "Total Price", "Status" );
        
        System.out.println( 
                "--------------------------------------------------------------------------------" );
        
        

        for (Reservation r : matches) {
            System.out.printf( "%-5d %-8d %-12s %-15s %-15s PHP %,-11.2f %-15s%n",
                    r.getReservationId(),
                    r.getRoom().getRoomNumber(),
                    r.getRoom().getRoomType(),
                    r.getCheckIn(),
                    r.getCheckOut(),
                    r.getTotalPrice(),
                    r.getStatus() );
        }
        
        System.out.println( 
                "\n================================================================================" );

        reservationId = readInt("Enter reservation ID to cancel: ");

        Reservation reservation
                = ReservationDAO.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            pauseScreen();
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation does not belong to you");
            pauseScreen();
            return;
        }
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            System.out.println(
                    "Cannot cancel. Reservation status is: "
                    + reservation.getStatus());
            pauseScreen();
            return;

        }

        boolean cancelled = ReservationDAO.cancelReservation(reservationId);

        if (cancelled) {
            boolean roomUpdated = RoomDAO.updateRoomStatus(
                    reservation.getRoom().getRoomNumber(),
                    "AVAILABLE"
            );
            if (roomUpdated) {
                System.out.println("Reservation cancelled successfully!");

            } else {
                System.out.println("Reservation was cancelled but the room status could not be updated.");

            }
            pauseScreen();

        } else {
            System.out.println("Failed to cancel reservation");
            pauseScreen();
        }
    }

    public static void checkInMenu() {

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = GuestDAO.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            pauseScreen();
            return;
        }

        ArrayList<Reservation> matches = ReservationDAO.findReservationsByGuestId(guest.getGuestId());

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            pauseScreen();
            return;
        }

        for (Reservation r : matches) {
            System.out.println(r);
        }

        int reservationId = readInt("Enter reservation ID to check in: ");

        Reservation reservation = ReservationDAO.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            pauseScreen();
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation does not belong to you");
            pauseScreen();
            return;
        }
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            System.out.println(
                    "Cannot check in. Reservation status is: "
                    + reservation.getStatus());
            pauseScreen();
            return;

        }

        boolean updated = ReservationDAO.updateReservationStatus(reservationId, "CHECKED_IN");

        if (updated) {
            boolean roomUpdated = RoomDAO.updateRoomStatus(
                    reservation.getRoom().getRoomNumber(), "OCCUPIED");
            if (roomUpdated) {
                clearScreen();
                System.out.println("Guest checked in successfully!");

            } else {
                System.out.println(
                        "Room status could not be updated. Check in was cancelled.");
                ReservationDAO.updateReservationStatus(reservationId, "CONFIRMED");

            }
            pauseScreen();

        } else {
            System.out.println("Failed to check in guest.");
            pauseScreen();
        }
    }

    public static void checkOutMenu() {

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = GuestDAO.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            pauseScreen();
            return;
        }

        ArrayList<Reservation> matches = ReservationDAO.findReservationsByGuestId(guest.getGuestId());

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            pauseScreen();
            return;
        }

        for (Reservation r : matches) {
            System.out.println(r);
        }

        int reservationId = readInt("Enter reservation ID to check out: ");

        Reservation reservation = ReservationDAO.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            pauseScreen();
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation does not belong to you");
            pauseScreen();
            return;
        }
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            System.out.println(
                    "Cannot check out. Reservation status is: "
                    + reservation.getStatus());
            pauseScreen();
            return;

        }

        boolean updated = ReservationDAO.updateReservationStatus(reservationId, "CHECKED_OUT");
        if (updated) {
            boolean roomUpdated = RoomDAO.updateRoomStatus(reservation.getRoom().getRoomNumber(), "AVAILABLE");

            if (roomUpdated) {
                clearScreen();
                System.out.println("Guest checked out successfully!");

            } else {
                System.out.println(
                        "Room status could not be updated. Check out was cancelled");
                ReservationDAO.updateReservationStatus(reservationId, "CHECKED_IN");

            }
            pauseScreen();

        } else {
            System.out.println("Failed to check out guest.");
            pauseScreen();

        }
    }

    public static void reservationReceipt(Reservation reservation, double payment, double change) {
        System.out.println();
        System.out.println("========== RESERVATION RECEIPT ==========\n");
        System.out.printf( "%-18s: %d%n", "Reservation ID", reservation.getReservationId());
        System.out.printf( "%-18s: %s%n", "Guest", reservation.getGuest().getName());
        System.out.printf( "%-18s: %s%n", "Email", reservation.getGuest().getEmail());
        System.out.printf( "%-18s: %s%n", "Phone", reservation.getGuest().getPhone());
        System.out.println();
        System.out.printf( "%-18s: %d%n", "Room Number", reservation.getRoom().getRoomNumber());
        System.out.printf( "%-18s: %s%n", "Room Type", reservation.getRoom().getRoomType());
        System.out.printf( "%-18s: PHP %,.2f%n", "Price Per Night",
                reservation.getRoom().getPricePerNight());
        System.out.printf( "%-18s: %s%n", "Check-in", reservation.getCheckIn());
        System.out.printf( "%-18s: %s%n", "Check-out", reservation.getCheckOut());
        System.out.printf( "%-18s: %d%n", "Number of Nights", reservation.getNumberOfNights());
        System.out.printf( "%-18s: PHP %,.2f%n", "Total Price", reservation.getTotalPrice());
        System.out.printf( "%-18s: PHP %,.2f%n", "Payment", payment);
        System.out.printf( "%-18s: PHP %,.2f%n", "Change", change);
        System.out.printf( "%-18s: %s%n", "Status", reservation.getStatus());
        System.out.println( "\n==========================================" );

    }

    public static void viewAvailableRoomsFromDatabase() {
        ArrayList<Room> rooms = RoomDAO.findAllRooms();
        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            clearScreen();
            System.out.println("No rooms available");
            pauseScreen();
            return;
        }

        int pageSize = 10;
        int currentPage = 0;
        int totalPages = (int) Math.ceil(
                (double) availableRooms.size() / pageSize
        );

        while (true) {
            clearScreen();

            System.out.println("===== AVAILABLE ROOMS =====\n");

            System.out.printf(
                    "%-12s %-12s %-15s %-12s%n",
                    "Room No.",
                    "Type",
                    "Price/Night",
                    "Status"
            );

            System.out.println("-----------------------------------------------");

            int start = currentPage * pageSize;
            int end = Math.min(
                    start + pageSize,
                    availableRooms.size()
            );

            for (int i = start; i < end; i++) {
                Room room = availableRooms.get(i);

                System.out.printf(
                        "%-12d %-12s PHP %-11.2f %-12s%n",
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getPricePerNight(),
                        room.getStatus()
                );
            }

            System.out.println("\nPage " + (currentPage + 1)
                    + " of " + totalPages);

            if (totalPages == 1) {
                pauseScreen();
                break;
            }

            System.out.println("\n[N] Next Page");
            System.out.println("[P] Previous Page");
            System.out.println("[B] Back");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("N")) {

                if (currentPage < totalPages - 1) {
                    currentPage++;
                } else {
                    System.out.println("Already on the last page.");
                    pauseScreen();
                }

            } else if (choice.equals("P")) {

                if (currentPage > 0) {
                    currentPage--;
                } else {
                    System.out.println("Already on the first page.");
                    pauseScreen();
                }

            } else if (choice.equals("B")) {

                break;

            } else {

                System.out.println("Invalid choice. Choose N, P, or B.");
                pauseScreen();
            }
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;

            } else {
                System.out.println("Invalid input! Enter a number only.");
                scanner.next();

            }

        }

    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;

            } else {
                System.out.println("Invalid input! Enter a number only.");
                scanner.next();

            }

        }

    }

    public static void pauseScreen() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to clear screen.");
        }
    }

}
