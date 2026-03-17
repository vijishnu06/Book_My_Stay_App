import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        isActive = false;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + ", RoomType: " + roomType +
                ", RoomID: " + roomId + ", Status: " + (isActive ? "Active" : "Cancelled");
    }
}

// Cancellation Service
class CancellationService {

    private Map<String, Reservation> reservations;
    private Map<String, Integer> inventory;
    private Stack<String> rollbackStack;

    public CancellationService(Map<String, Reservation> reservations,
                               Map<String, Integer> inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        // Validate existence
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation does not exist.");
            return;
        }

        Reservation res = reservations.get(reservationId);

        // Prevent duplicate cancellation
        if (!res.isActive()) {
            System.out.println("Cancellation Failed: Booking already cancelled.");
            return;
        }

        // Step 1: Push room ID to stack (LIFO rollback tracking)
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        String roomType = res.getRoomType();
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Step 3: Mark reservation as cancelled
        res.cancel();

        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (Recently Released Room IDs): " + rollbackStack);
    }
}

// Main class
public class Book_My_Stay {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);

        // Sample reservations
        Map<String, Reservation> reservations = new HashMap<>();
        reservations.put("R101", new Reservation("R101", "Single", "S1"));
        reservations.put("R102", new Reservation("R102", "Double", "D1"));
        reservations.put("R103", new Reservation("R103", "Suite", "SU1"));

        // Reduce inventory to simulate confirmed bookings
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 0);

        CancellationService service = new CancellationService(reservations, inventory);

        while (true) {

            System.out.println("\n1. Cancel Booking");
            System.out.println("2. View Reservations");
            System.out.println("3. View Inventory");
            System.out.println("4. View Rollback Stack");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String id = sc.next();
                    service.cancelBooking(id);
                    break;

                case 2:
                    System.out.println("\n--- Reservations ---");
                    for (Reservation r : reservations.values()) {
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
                    service.showRollbackStack();
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}