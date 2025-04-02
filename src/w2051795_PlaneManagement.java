import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

import static java.lang.System.out;

public class w2051795_PlaneManagement {
    private static int[] seats;
    private static final Ticket[] soldTickets = new Ticket[52]; // Use array for sold seats (52 seats only)
    private static int soldTicketsCount = 0;
    private static final int ROWS = 14;
    private static final int ROWS_A_D = 14;  // A,D 14 seats
    private static final int ROWS_B_C = 12;  // B,C 12 seats
    private static final int SEATS_PER_ROW = 4;


    // The main method that starts the Plane Management application.
    public static void main(String[] args) {
        initializeSeats();
        out.println("\nWelcome to the Plane Management application");

        while (true) {
            displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 0:
                    out.println("Exiting the Plane Management application.\nHave a good day!");
                    System.exit(0);
                case 1:
                    buy_seat();
                    break;
                case 2:
                    cancel_seat();
                    break;
                case 3:
                    find_first_available();
                    break;
                case 4:
                    show_seating_plan();
                    break;
                case 5:
                    print_tickets_info();
                    break;
                case 6:
                    search_ticket();
                    break;
                default:
                    out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Displays the menu options for the Plane Management application.
    private static void displayMenu() {
        out.println("\n**************************************************");
        out.println("*                  MENU OPTIONS                  *");
        out.println("**************************************************");
        out.println("1) Buy a seat");
        out.println("2) Cancel a seat");
        out.println("3) Find first available seat");
        out.println("4) Show seating plan");
        out.println("5) Print tickets information and total sales");
        out.println("6) Search ticket");
        out.println("0) Quit");
        out.println("****************************************************");
    }

    // Gets the user's choice from the menu.
    private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                out.print("Please select an option: ");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice <= 6) {
                    return choice;
                } else {
                    out.println("Invalid choice. Please enter a correct number.(0-6 choices)");   // Enter a number (except 0-6)
                }
            } catch (NumberFormatException e) {
                out.println("Invalid choice. Please enter a number(0-6).");  // Enter choice except a number
            }
        }
    }

    //Initializes the seats array.
    private static void initializeSeats() {
        seats = new int[ROWS * SEATS_PER_ROW];
        Arrays.fill(seats, 0);
    }

    // Allows buy a seat.
    private static void buy_seat() {
        Scanner scanner = new Scanner(System.in);
        out.print("Enter the row letter (A-D): ");
        char columnLetter = scanner.next().toUpperCase().charAt(0);
        if (columnLetter < 'A' || columnLetter > 'D') {
            out.println("Invalid row letter. Please enter correct row letter (A,B,C or D)");  // Enter invalid input
            return;
        }

        out.print("Enter the seat number above row (1-14): ");
        int rowNumber;
        while (true) {
            try {
                rowNumber = Integer.parseInt(scanner.next());
                if (rowNumber >= 1 && rowNumber <= 14) {
                    break;
                } else {
                    out.println("Invalid seat number. Please enter a seat number between 1 and 14.");  // Enter a number (except 1-14)
                    return;
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input. Please enter a seat number.(1-14)");  // Enter invalid import (except number)
                return;
            }
        }

        if ((columnLetter == 'B' || columnLetter == 'C') && rowNumber > 12) {  // invalid input (row B and C have 12 seats only)
            out.println("Row B and C have 12 seats only. Please select a row number between 1 and 12.");
            return;
        }

        int seatIndex = getSeatIndex(rowNumber, columnLetter);
        if (isValidSeat(seatIndex)) {
            if (seats[seatIndex] == 1) {
                out.println("Sorry,This seat is already booked.");
                return;
            }
            out.print("--- please enter passenger details ---");
            out.print("\nEnter your name: ");
            String name = scanner.next();
            out.print("Enter your surname: ");
            String surname = scanner.next();
            out.print("Enter your e-mail: ");
            String email = scanner.next();

            Person person = new Person(name, surname, email);
            double price = calculateTicketPrice(rowNumber);
            Ticket ticket = new Ticket(person, rowNumber, columnLetter, price);
            soldTickets[soldTicketsCount++] = ticket;
            ticket.save();  // save ticket information to a file
            seats[seatIndex] = 1;
            out.println("Seat purchased successfully!\nThank you for joining us.");
        } else {
            out.println("Invalid seat. Please enter a valid seat.");
        }
    }

    // Allows cancel a seat.
    private static void cancel_seat() {
        Scanner scanner = new Scanner(System.in);
        out.print("Enter the row letter (A-D): ");
        char columnLetter = scanner.next().toUpperCase().charAt(0);
        if (columnLetter < 'A' || columnLetter > 'D') {
            out.println("Invalid row letter. Please enter valid row letter (A,B,C,orD)");  // Enter invalid input
            return;
        }

        out.print("Enter the seat number above row (1-14): ");
        int rowNumber;
        while (true) {
            try {
                rowNumber = Integer.parseInt(scanner.next());
                if (rowNumber >= 1 && rowNumber <= 14) {
                    break;
                } else {
                    out.println("Invalid seat number. Please enter a seat number (1-14)");// Enter a number (except 1-14)
                    return;
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input. Please enter a seat number.");// Enter invalid import (except number)
                return;
            }
        }

        int seatIndex = getSeatIndex(rowNumber, columnLetter);
        if (isValidSeat(seatIndex) && seats[seatIndex] == 1) {
            seats[seatIndex] = 0;

            for (int i = 0; i < soldTicketsCount; i++) {
                if (soldTickets[i].rowNumber() == rowNumber && soldTickets[i].columnLetter() == columnLetter) {
                    for (int j = i; j < soldTicketsCount - 1; j++) {
                        soldTickets[j] = soldTickets[j + 1];
                    }
                    soldTicketsCount--;
                    out.println("Seat cancelled successfully!");

                    // Delete ticket file
                    String fileName= String.format("Ticket_%d_%c.txt", rowNumber, columnLetter);
                    File file = new File(fileName);
                    if (file.exists()) {
                        if (file.delete()) {
                            out.println("Ticket file deleted successfully.");
                        } else {
                            out.println("Failed to delete ticket file.");
                        }
                    } else {
                        out.println("Ticket file not found.");
                    }
                    return;
                }
            }

            out.println("Ticket not found.");  // Invalid input
        } else if ((columnLetter == 'B' || columnLetter == 'C') && rowNumber > 12) {  // Invalid input (row B and C have 12 seats only)
            out.println("row B and C have 12 seats only. Please select a row number between 1 and 12.");
        } else {
            out.println("Not booked. This seat is already available");  // Not sold seat choice input
        }
    }

    // Finds the first available seat

    private static void find_first_available() {
        boolean seatFound = false; // Flag to track, if an available seat is found
        for (char row = 'A'; row <= 'D'; row++) {
            int rowCount = (row == 'B' || row == 'C') ? ROWS_B_C : ROWS_A_D;
            for (int col = 1; col <= rowCount; col++) {
                int seatIndex = getSeatIndex(col, row);
                if (isValidSeat(seatIndex) && seats[seatIndex] == 0) {
                    out.println("First available seat: Row letter " + row + " , Seat number " + col  );
                    seatFound = true;
                    break;
                }
            }
            if (seatFound) {   // Exit outer loop if seat found
                break;
            }
        }
        if (!seatFound) {
            out.println("No available seats."); //All seats are already booked
        }
    }

    // show seat plan
        private static void show_seating_plan () {
            out.println("-- Seating Plan --");
            out.println("Available seats: O");
            out.println("Sold seats: X");
            out.println("    1   2   3   4   5   6   7   8   9   10  11  12  13  14");  // identify seats numbers

            char[] columns = {'A', 'B', 'C', 'D'};
            for (char col : columns) {
                out.print(col + "  ");
                for (int row = 1; row <= 14; row++) {
                    int seatIndex = getSeatIndex(row, col);
                    if (col == 'A' || col == 'D') { // A,D row seats filled or not showSeatingPlan
                        out.print(seats[seatIndex] == 0 ? " O  " : " X  ");
                    } else if (row <= 12) { // B,C row seats filled or not showSeatingPlan
                        out.print(seats[seatIndex] == 0 ? " O  " : " X  ");
                    } else {
                        out.print("     ");   // Space on seats
                    }
                }
                out.println();
            }
        }

        // Prints tickets information and total sales.
        private static void print_tickets_info () {
            double totalPrice = 0;
            out.println("Sold tickets information");
            out.println("----------------------");
            for (int i = 0; i < soldTicketsCount; i++) {
                Ticket ticket = soldTickets[i];
                out.println("Passenger Information:");
                ticket.passenger().printInfo();
                out.println("Row letter: " + ticket.columnLetter());
                out.println("Seat number: " + ticket.rowNumber());
                double price = calculateTicketPrice(ticket.rowNumber());
                totalPrice += price;
                out.println("Ticket Price: £" + price);
                out.println("----------------------");
            }
            out.println("\nTotal amount of Tickets Sold: £" + totalPrice);
        }

        // To search a specific ticket
        private static void search_ticket () {
            Scanner scanner = new Scanner(System.in);
            out.print("Enter the column letter (A-D): ");
            char columnLetter = scanner.next().toUpperCase().charAt(0);
            if (columnLetter < 'A' || columnLetter > 'D') {
                out.println("Invalid column letter. Please enter A, B, C, or D.");  // Enter invalid input
                return;
            }

            out.print("Enter the row number (1-14): ");
            int rowNumber;
            while (true) {
                try {
                    rowNumber = Integer.parseInt(scanner.next());
                    if (rowNumber >= 1 && rowNumber <= 14) {
                        break;
                    } else {
                        out.println("Invalid seat number. Please enter a number between 1 and 14.");  //Enter a number (except 1-14)
                    }
                } catch (NumberFormatException e) {
                    out.println("Invalid input. Please enter a number. (1-14)");  //Enter invalid input (except number)
                }
            }
            if ((columnLetter == 'B' || columnLetter == 'C') && rowNumber > 12) {
                out.println("Rows B and C have 12 seats only. Please select a row number between 1 and 12.");  // invalid input (row B and C have 12 seats only)
                return;
            }

            // show seat,passenger information for specific seat
            for (int i = 0; i < soldTicketsCount; i++) {
                Ticket ticket = soldTickets[i];
                if (ticket.rowNumber() == rowNumber && ticket.columnLetter() == columnLetter) {
                    out.println("This seat is sold");
                    out.println("Passenger Information:");
                    ticket.passenger().printInfo();
                    out.println("Row letter: " + ticket.columnLetter());
                    out.println("Seat number: " + ticket.rowNumber());
                    return;
                }
            }
            out.println("This seat is available.");  // if seat not sold
        }

        private static int getSeatIndex ( int rowNumber, char columnLetter){
            if (rowNumber < 1 || rowNumber > 14 || columnLetter < 'A' || columnLetter > 'D') {
                return -1;  // Invalid seat index
            }

            int rowOffset = (rowNumber - 1) * 4;
            int columnOffset = columnLetter - 'A';
            return rowOffset + columnOffset;
        }

        private static boolean isValidSeat ( int seatIndex){
            return seatIndex >= 0 && seatIndex < seats.length;
        }  // checks if a given seat index is valid.

        // ticket prices
        private static double calculateTicketPrice ( int rowNumber){
            if (rowNumber >= 1 && rowNumber <= 5) {
                return 200;
            } else if (rowNumber >= 6 && rowNumber <= 9) {
                return 150;
            } else {
                return 180;
            }

        }
    }







