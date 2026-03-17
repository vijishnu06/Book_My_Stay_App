import java.util.*;

// Custom Exception for invalid booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Validator class
class InvalidBookingValidator {

    private static final List<String> VALID_ROOM_TYPES =
            Arrays.asList("Single", "Double", "Suite");

    public static void validate(String roomType, int roomsRequested, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate number of rooms
        if (roomsRequested <= 0) {
            throw new InvalidBookingException("Rooms requested must be greater than 0.");
        }

        // Validate availability
        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        if (roomsRequested > available) {
            throw new InvalidBookingException("Requested rooms exceed available rooms.");
        }
    }
}

// Booking system
class BookingSystem {

    private Map<String, Integer> inventory;

    public BookingSystem() {
        inventory = new HashMap<>();

        // Initial inventory
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }

    public void bookRoom(String roomType, int roomsRequested) {
        try {
            // Validate input (Fail-Fast)
            InvalidBookingValidator.validate(roomType, roomsRequested, inventory);

            // Update inventory only if valid
            inventory.put(roomType, inventory.get(roomType) - roomsRequested);

            System.out.println("Booking successful for " + roomsRequested + " " + roomType + " room(s).");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main class
public class Book_My_Stay_App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingSystem system = new BookingSystem();

        while (true) {
            system.displayInventory();

            System.out.println("\nEnter room type (Single/Double/Suite) or 'exit' to quit:");
            String roomType = sc.nextLine();

            if (roomType.equalsIgnoreCase("exit")) {
                System.out.println("Exiting system...");
                break;
            }

            System.out.print("Enter number of rooms: ");
            int rooms = 0;

            try {
                rooms = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a valid number.");
                continue;
            }

            // Attempt booking
            system.bookRoom(roomType, rooms);
        }

        sc.close();
    }
}