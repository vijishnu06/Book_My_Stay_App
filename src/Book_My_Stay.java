import java.util.*;

// Booking Request class
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // notify waiting threads
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait if queue empty
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.poll();
    }
}

// Shared Booking System (Critical Section)
class BookingSystem {

    private Map<String, Integer> inventory;

    public BookingSystem() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Critical Section (Thread-Safe)
    public synchronized void processBooking(BookingRequest request) {

        String roomType = request.roomType;

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate processing delay
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            inventory.put(roomType, available - 1);

            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS: " + request.guestName +
                    " booked " + roomType);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: No " + roomType +
                    " rooms available for " + request.guestName);
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private BookingSystem system;

    public BookingProcessor(BookingQueue queue, BookingSystem system, String name) {
        super(name);
        this.queue = queue;
        this.system = system;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = queue.getRequest();
            system.processBooking(request);
        }
    }
}

// Main Class
public class Book_My_Stay_App {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        BookingSystem system = new BookingSystem();

        // Create worker threads
        BookingProcessor t1 = new BookingProcessor(queue, system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, system, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(queue, system, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Simulate multiple guests (concurrent requests)
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));
        queue.addRequest(new BookingRequest("Charlie", "Single")); // extra → fail

        queue.addRequest(new BookingRequest("David", "Double"));
        queue.addRequest(new BookingRequest("Eve", "Double"));
        queue.addRequest(new BookingRequest("Frank", "Double")); // extra → fail

        queue.addRequest(new BookingRequest("Grace", "Suite"));
        queue.addRequest(new BookingRequest("Hank", "Suite")); // extra → fail

        // Allow threads to finish processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        system.displayInventory();

        System.exit(0);
    }
}