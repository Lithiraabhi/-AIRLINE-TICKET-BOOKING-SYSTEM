import java.io.FileWriter;
import java.io.IOException;

// represents a ticket containing information about the passenger, seat, and price.
public record Ticket(Person passenger, int rowNumber, char columnLetter, double price) {


    // Save method to file
    public void save() {
        String fileName = String.format("Ticket_%d_%c.txt", rowNumber, columnLetter);
        try (FileWriter writer = new FileWriter(fileName)) {    // Write ticket information to the file
            writer.write("-- Ticket Information --\n");
            writer.write("\nPassenger Information:\n");
            writer.write("Name: " + passenger.getName() + "\n");
            writer.write("Surname: " + passenger.getSurname() + "\n");
            writer.write("Email: " + passenger.getEmail() + "\n");
            writer.write("Seat row: " + columnLetter + "\n");
            writer.write("Seat number: " + rowNumber + "\n");
            writer.write("Ticket price: £" + price + "\n");
            System.out.println("Ticket information saved to file: " + fileName);
        } catch (IOException e) {  // IO exceptions Handle
            System.out.println("An error occurred while saving the ticket information to file: " + fileName);
            e.printStackTrace();
        }
    }


}

