import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType;
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void saveState(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

// Main class
public class Book_My_Stay_App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Map<String, Integer> inventory;
        List<Reservation> bookingHistory;

        // Load previous state
        SystemState state = PersistenceService.loadState();

        if (state != null) {
            inventory = state.inventory;
            bookingHistory = state.bookingHistory;
        } else {
            // Initialize default state
            inventory = new HashMap<>();
            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);

            bookingHistory = new ArrayList<>();
        }

        while (true) {
            System.out.println("\n1. Add Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Room Type: ");
                    String room = sc.nextLine();

                    // Simple inventory check
                    int available = inventory.getOrDefault(room, 0);

                    if (available > 0) {
                        Reservation res = new Reservation(id, name, room);
                        bookingHistory.add(res);

                        inventory.put(room, available - 1);

                        System.out.println("Booking added successfully.");
                    } else {
                        System.out.println("Booking failed: No rooms available.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Booking History ---");
                    for (Reservation r : bookingHistory) {
                        System.out.println(r);
                    }
                    break;

                case 3:
                    System.out.println("\n--- Inventory ---");
                    for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    break;

                case 4:
                    // Save state before exit
                    SystemState newState = new SystemState(inventory, bookingHistory);
                    PersistenceService.saveState(newState);

                    System.out.println("Exiting system...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}