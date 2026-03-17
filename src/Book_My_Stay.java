import java.util.*;

// Class representing an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manager class to handle Add-On Services for reservations
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }
}

// Main class
public class Book_My_Stay_App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        // Sample services
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1000);

        while (true) {
            System.out.println("\nSelect Add-On Services:");
            System.out.println("1. WiFi (₹200)");
            System.out.println("2. Breakfast (₹500)");
            System.out.println("3. Airport Pickup (₹1000)");
            System.out.println("4. Finish Selection");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, wifi);
                    System.out.println("WiFi added.");
                    break;

                case 2:
                    manager.addService(reservationId, breakfast);
                    System.out.println("Breakfast added.");
                    break;

                case 3:
                    manager.addService(reservationId, airportPickup);
                    System.out.println("Airport Pickup added.");
                    break;

                case 4:
                    System.out.println("\n--- Summary ---");
                    manager.displayServices(reservationId);

                    double total = manager.calculateTotalServiceCost(reservationId);
                    System.out.println("Total Add-On Cost: ₹" + total);
                    System.out.println("\nCore booking remains unchanged.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}