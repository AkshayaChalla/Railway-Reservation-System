import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Train {
    String trainName;
    String route;
    int availableSeats;
    Queue<String> waitlist;  // Queue to store waitlisted users

    // Constructor to initialize train details
    Train(String name, String route) {
        this.trainName = name;
        this.route = route;
        this.availableSeats = 100;  // each train starts with 100 seats
        this.waitlist = new LinkedList<>();
    }

    // Method to book a seat
    String bookSeat(String username) {
        if (availableSeats > 0) {
            availableSeats--;
            return "Booking confirmed on " + trainName + " for " + username;
        } else if (waitlist.size() < 10) {  // Max 10 people can be waitlisted
            waitlist.add(username);
            return "Train is full. " + username + " has been added to the waitlist for " + trainName;
        } else {
            return "Sorry, no seats available and the waitlist is full.";
        }
    }

    // Method to cancel a seat
    String cancelSeat(String username) {
        if (availableSeats < 100) {
            availableSeats++;
            return "Booking cancelled successfully on " + trainName + " for " + username;
        } else if (!waitlist.isEmpty()) {
            String upgradedUser = waitlist.poll();  // Upgrade the first user in the waitlist
            return "Booking cancelled. " + username + "'s seat has been freed up, and " + upgradedUser +
                    " from the waitlist has been upgraded to a confirmed booking.";
        } else {
            return "No booking to cancel for " + username + ".";
        }
    }

    // Display the train status
    void displayStatus() {
        System.out.println(trainName + " (Route: " + route + ")");
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("Waitlisted Users: " + waitlist.size());
    }

    // Check if a user is on the waitlist
    boolean isUserOnWaitlist(String username) {
        return waitlist.contains(username);
    }
}

public class RailwayReservationSystem {
    static Scanner scanner = new Scanner(System.in);
    static Train[] trains = new Train[5];
    static String storedUsername = "mani";
    static String storedPassword = "2006";
    static String securityAnswer = "blue"; // Security question answer
    static String[] userBookings = new String[5];  // Tracks which train a user is booked on (or null if no booking)

    public static void main(String[] args) {
        // Initialize 5 trains
        trains[0] = new Train("Train A", "MHBD to WGL");
        trains[1] = new Train("Train B", "WGL TO MHBD");
        trains[2] = new Train("Train C", "HYD TO MANUGOOR");
        trains[3] = new Train("Train D", "MAMUNOOR TO WGL");
        trains[4] = new Train("Train E", "WGL TO KMM");

        // User login
        if (!login()) {
            System.out.println("Invalid login details. Exiting program.");
            return;
        }

        // Main menu
        int choice;
        do {
            System.out.println("\nRailway Reservation System");
            System.out.println("1. View Train Seat Availability");
            System.out.println("2. Book a Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Forgot Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewSeatAvailability();
                    break;
                case 2:
                    bookSeat();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    forgotPassword();
                    break;
                case 5:
                    System.out.println("Exiting the system. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
    }

    // Login method
    static boolean login() {
        String username, password;
        System.out.print("Enter username: ");
        username = scanner.next();
        System.out.print("Enter password: ");
        password = scanner.next();

        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    // Method to view seat availability for all trains
    static void viewSeatAvailability() {
        System.out.println("\nSeat Availability:");
        for (Train train : trains) {
            train.displayStatus();
        }
    }

    // Method to book a seat on a train
    static void bookSeat() {
        if (isAlreadyBooked()) {
            System.out.println("You have already booked a seat on another train. Please cancel the current booking to book a new one.");
            return;
        }

        System.out.println("\nAvailable trains to book a seat:");
        for (int i = 0; i < trains.length; i++) {
            System.out.println((i + 1) + ". " + trains[i].trainName + " (Route: " + trains[i].route + ")");
        }
        System.out.print("Enter the train number to book a seat (1-5): ");
        int trainChoice = scanner.nextInt();

        if (trainChoice < 1 || trainChoice > 5) {
            System.out.println("Invalid train number.");
            return;
        }

        System.out.print("Enter your username: ");
        scanner.nextLine();  // Consume newline character
        String username = scanner.nextLine();

        if (userBookings[trainChoice - 1] != null) {
            System.out.println("You have already booked a seat on this train.");
            return;
        }

        // Book a seat on the selected train
        String bookingMessage = trains[trainChoice - 1].bookSeat(username);
        System.out.println(bookingMessage);

        // If booking is confirmed, mark the user as having booked on this train
        if (bookingMessage.contains("confirmed")) {
            userBookings[trainChoice - 1] = username;
        }
    }

    // Method to cancel an existing booking
    static void cancelBooking() {
        System.out.print("\nEnter your username: ");
        scanner.nextLine();  // Consume newline character
        String username = scanner.nextLine();

        // Find the train the user is booked on
        boolean foundBooking = false;
        for (int i = 0; i < trains.length; i++) {
            if (userBookings[i] != null && userBookings[i].equals(username)) {
                foundBooking = true;
                // Cancel the booking
                String cancellationMessage = trains[i].cancelSeat(username);
                System.out.println(cancellationMessage);

                // Clear the booking record for this user
                userBookings[i] = null;
                break;
            }
        }

        if (!foundBooking) {
            System.out.println("No booking found for " + username);
        }
    }

    // Method to handle the forgot password scenario
    static void forgotPassword() {
        System.out.println("\nForgot Password");

        // Ask the security question
        System.out.print("What is your favorite color? ");
        scanner.nextLine(); // Consume newline character
        String answer = scanner.nextLine();

        if (answer.equalsIgnoreCase(securityAnswer)) {
            System.out.println("Security question answered correctly.");
            System.out.print("Enter a new password: ");
            storedPassword = scanner.nextLine();
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Incorrect answer to the security question.");
        }
    }

    // Method to check if the user has already booked a seat on any train
    static boolean isAlreadyBooked() {
        for (String booking : userBookings) {
            if (booking != null) {
                return true;
            }
        }
        return false;
    }
}
