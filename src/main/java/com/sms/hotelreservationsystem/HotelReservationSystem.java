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

public class HotelReservationSystem {

    static Hotel hotel = new Hotel();
    static Scanner scanner = new Scanner(System.in);
    static int nextReservationId = 1;
    static int nextGuestId = 1;

    public static void main(String[] args) {
        for (int i = 101; i <= 110; i++) {
            hotel.addRoom(new Room(i, "SINGLE", 1500));

        }
        for (int i = 201; i <= 210; i++) {
            hotel.addRoom(new Room(i, "DELUXE", 2500));

        }
        for (int i = 301; i <= 310; i++) {
            hotel.addRoom(new Room(i, "PREMIUM", 3500));
        }
        for (int i = 401; i <= 410; i++) {
            hotel.addRoom(new Room(i, "SUITE", 4500));
        }

        boolean running = true;

        do {
            int choice = displayMenu();

            switch (choice) {
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
        System.out.println("\n===== HOTEL RESERVATION =====");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Search Room By Number");
        System.out.println("3. Search Rooms By Type");
        System.out.println("4. Make Reservation");
        System.out.println("5. Reservation Management");
        System.out.println("6. Check In");
        System.out.println("7. Check Out");
        System.out.println("8. Exit");

        System.out.print("Enter choice (1-8 only): ");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } else {
            System.out.println("Invalid input! Enter a number only!");
            scanner.next();
            return 0;
        }
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
            System.out.print("Enter choice(1-4): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Invalid input. Enter a number only!");
                scanner.next();
            }

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

        scanner.nextLine();

        ArrayList<Room> matches = hotel.findRoomsByType(roomType);

        Room assignedRoom = null;

        for (Room r : matches) {
            if (r.isAvailable()) {
                assignedRoom = r;
                break;
            }
        }

        if (assignedRoom == null) {
            System.out.println("No rooms of that type are currently available");
            return;
        }

        int reservationId = nextReservationId;

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = hotel.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("No email found. Lets create one!");

            int guestId = nextGuestId;

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();

            guest = new Guest(guestId, name, phone, email);
            hotel.addGuest(guest);

            nextGuestId++;
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

            System.out.print("Enter payment: PHP ");

            if (scanner.hasNextDouble()) {
                payment = scanner.nextDouble();
                scanner.nextLine();

                if (payment <= 0) {
                    System.out.println("Invalid payment! Payment must be greater than 0.");

                } else if (payment >= totalPrice) {
                    double change = payment - totalPrice;

                    hotel.createReservation(
                            reservationId,
                            guest.getGuestId(),
                            assignedRoom.getRoomNumber(),
                            checkIn,
                            checkOut
                    );

                    Reservation reservation = hotel.findReservationById(reservationId);

                    reservationReceipt(reservation, payment, change);

                    nextReservationId++;

                    validPayment = true;

                } else {
                    System.out.println("Insufficient payment. Please enter enough payment");
                }

            } else {
                System.out.println("Invalid payment. Enter a number only");
                scanner.next();
            }
        } while (!validPayment);

    }

    public static void searchRoomByNumber() {
        int roomNumber = 0;
        System.out.print("Enter room number: ");
        if (scanner.hasNextInt()) {
            roomNumber = scanner.nextInt();
            Room room = hotel.findRoomByNumber(roomNumber);
            if (room == null) {
                System.out.println("Room not found!");
            } else {
                System.out.println(room);
            }
        } else {
            System.out.println("Enter a number only");
            scanner.next();
        }
    }

    public static void searchRoomsByType() {
        String roomType = "";
        int choice = 0;
        boolean validType = false;

        do {
            System.out.println("Choose Room Type");
            System.out.println("1. SINGLE\n2. DELUXE\n3. PREMIUM\n4. SUITE");
            System.out.println("Enter choice(1-4 only): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Enter a number only!");
                scanner.next();
            }

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
        ArrayList<Room> matches = hotel.findRoomsByType(roomType);
        if (matches.isEmpty()) {
            System.out.println("No rooms found");
        } else {
            for (Room r : matches) {
                System.out.println(r);

            }

        }

    }

    public static void reservationManagementMenu() {
        int choice = 0;
        boolean running = true;

        do {
            System.out.println("---RESERVATION MANAGEMENT---");
            System.out.println("""
                               1. View my reservation
                               2. Update my reservation
                               3. Cancel my reservation
                               4. Back""");
            System.out.print("Enter choice(1-4 only): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input! Only put a number");
                scanner.next();
            }

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
            }

        } while (running);

    }

    public static void viewMyReservations() {
        String email = "";
        System.out.println("Enter your email: ");
        email = scanner.nextLine();
        Guest guest = hotel.findGuestByEmail(email);
        if (guest == null) {
            System.out.println("Guest not found");
        } else {
            ArrayList<Reservation> matches = hotel.findReservationsByGuest(guest);
            if (matches.isEmpty()) {
                System.out.println("No reservations found");
            } else {
                for (Reservation r : matches) {
                    System.out.println(r);

                }

            }
        }

    }

    public static void updateMyReservation() {
        int reservationId;

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = hotel.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            return;
        }

        ArrayList<Reservation> matches = hotel.findReservationsByGuest(guest);

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            return;
        }

        for (Reservation r : matches) {
            System.out.println(r);
        }

        System.out.print("Enter reservation ID to update: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Put a number only!");
            scanner.next();
            return;
        }

        reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = hotel.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation doesnt belong to you");
            return;
        }

        System.out.print("Enter new check-in: ");
        String inputCheckIn = scanner.nextLine();

        System.out.print("Enter new check-out: ");
        String inputCheckOut = scanner.nextLine();

        try {
            LocalDate newCheckIn = LocalDate.parse(inputCheckIn);
            LocalDate newCheckOut = LocalDate.parse(inputCheckOut);

            if (!newCheckIn.isBefore(newCheckOut)) {
                System.out.println("Invalid date! Check-out must be after check-in.");
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
        }
    }

    public static void handlePaymentAdjustment(Reservation reservation,
            LocalDate newCheckIn, LocalDate newCheckOut, double newTotalPrice) {

        double paymentDifference = newTotalPrice - reservation.getPayment();

        if (paymentDifference > 0) {

            System.out.println("Additional payment required: PHP " + paymentDifference);
            System.out.print("Enter additional payment: PHP ");

            if (scanner.hasNextDouble()) {
                double payment = scanner.nextDouble();
                scanner.nextLine();

                if (payment >= paymentDifference) {
                    double change = payment - paymentDifference;

                    System.out.println("Additional payment accepted");
                    System.out.println("Change: PHP " + change);

                    reservation.updateDates(newCheckIn, newCheckOut);
                    reservation.updateTotalPrice();
                    reservation.setPayment(
                            reservation.getPayment() + paymentDifference
                    );

                    System.out.println("Reservation updated successfully!");
                    System.out.println(reservation);

                } else {
                    System.out.println("Insufficient additional payment");
                }

            } else {
                System.out.println("Invalid payment. Enter a number only");
                scanner.next();
            }

        } else if (paymentDifference == 0) {

            System.out.println("No additional payment required");

            reservation.updateDates(newCheckIn, newCheckOut);
            reservation.updateTotalPrice();

            System.out.println("Reservation updated successfully!");
            System.out.println(reservation);

        } else {

            double refund = reservation.getPayment() - newTotalPrice;

            System.out.println("Reservation total decreased.");
            System.out.println("Refund amount: PHP " + refund);

            reservation.updateDates(newCheckIn, newCheckOut);
            reservation.updateTotalPrice();
            reservation.setPayment(newTotalPrice);

            System.out.println("Reservation updated successfully!");
            System.out.println(reservation);
        }
    }

    public static void cancelReservation() {
        int reservationId;

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = hotel.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
        } else {
            ArrayList<Reservation> matches = hotel.findReservationsByGuest(guest);

            if (matches.isEmpty()) {
                System.out.println("No reservation found");
            } else {
                for (Reservation r : matches) {
                    System.out.println(r);
                }

                System.out.print("Enter reservation ID to cancel: ");

                if (scanner.hasNextInt()) {
                    reservationId = scanner.nextInt();
                    scanner.nextLine();

                    Reservation reservation = hotel.findReservationById(reservationId);

                    if (reservation == null) {
                        System.out.println("Reservation ID does not exist");
                    } else {
                        if (reservation.getGuest().getGuestId() == guest.getGuestId()) {

                            hotel.cancelReservation(reservationId);
                            System.out.println("Reservation cancelled successfully");

                        } else {
                            System.out.println("This reservation does not belong to you");
                        }

                    }

                } else {
                    System.out.println("Invalid input! Put a number only!");
                    scanner.next();
                }
            }
        }
    }

    public static void checkInMenu() {

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = hotel.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            return;
        }

        ArrayList<Reservation> matches = hotel.findReservationsByGuest(guest);

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            return;
        }

        for (Reservation r : matches) {
            System.out.println(r);
        }

        System.out.print("Enter reservation ID to check in: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Put a number only!");
            scanner.next();
            return;
        }

        int reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = hotel.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation does not belong to you");
            return;
        }

        hotel.checkInGuest(reservationId);
    }

    public static void checkOutMenu() {

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Guest guest = hotel.findGuestByEmail(email);

        if (guest == null) {
            System.out.println("Guest not found");
            return;
        }

        ArrayList<Reservation> matches = hotel.findReservationsByGuest(guest);

        if (matches.isEmpty()) {
            System.out.println("No reservations found");
            return;
        }

        for (Reservation r : matches) {
            System.out.println(r);
        }

        System.out.print("Enter reservation ID to check out: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Put a number only!");
            scanner.next();
            return;
        }

        int reservationId = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = hotel.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation ID does not exist");
            return;
        }

        if (reservation.getGuest().getGuestId() != guest.getGuestId()) {
            System.out.println("This reservation does not belong to you");
            return;
        }

        hotel.checkOutGuest(reservationId);
    }

    public static void reservationReceipt(Reservation reservation, double payment, double change) {
        System.out.println("\n=======RESERVATION RECEIPT=======\n");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest: " + reservation.getGuest().getName());
        System.out.println("Email: " + reservation.getGuest().getEmail());
        System.out.println("Phone: " + reservation.getGuest().getPhone());

        System.out.println("Room Number: " + reservation.getRoom().getRoomNumber());
        System.out.println("Room Type: " + reservation.getRoom().getRoomType());
        System.out.println("Price Per Night: PHP " + reservation.getRoom().getPricePerNight());
        System.out.println("Check-in: " + reservation.getCheckIn());
        System.out.println("Check-out: " + reservation.getCheckOut());
        System.out.println("Number of Nights: " + reservation.getNumberOfNights());
        System.out.println("Total Price: PHP " + reservation.getTotalPrice());
        System.out.println("Payment: PHP " + payment);
        System.out.println("Change: PHP " + change);
        System.out.println("Status: " + reservation.getStatus());
        System.out.println("=========================================");

    }

}
