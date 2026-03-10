import java.util.*;

// Reservation request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Booking Service
class BookingService {

    private Queue<Reservation> queue = new LinkedList<>();
    private InventoryService inventory;

    // Track allocated room IDs
    private Set<String> allocatedRooms = new HashSet<>();

    // Map room type -> allocated rooms
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    // Counter for room IDs
    private Map<String, Integer> roomCounters = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public void processBookings() {

        System.out.println("Room Allocation Processing:");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();
            String type = r.getRoomType();

            if (!inventory.isAvailable(type)) {
                continue;
            }

            // Generate room ID like Single-1
            int count = roomCounters.getOrDefault(type, 0) + 1;
            roomCounters.put(type, count);

            String roomId = type + "-" + count;

            // Ensure uniqueness
            allocatedRooms.add(roomId);

            roomAllocations.putIfAbsent(type, new HashSet<>());
            roomAllocations.get(type).add(roomId);

            // Update inventory
            inventory.decrement(type);

            System.out.println("Booking confirmed for Guest: "
                    + r.getGuestName()
                    + ", Room ID: "
                    + roomId);
        }
    }
}

public class Book_My_Stay {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        BookingService booking = new BookingService(inventory);

        booking.addRequest(new Reservation("Abhi", "Single"));
        booking.addRequest(new Reservation("Subha", "Single"));
        booking.addRequest(new Reservation("Vanmathi", "Suite"));

        booking.processBookings();
    }
}