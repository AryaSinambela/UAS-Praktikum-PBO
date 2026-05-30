package pbo.f01;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.*;

/**
 * Driver class utama
 * Nama: Arya Pratama Sinambela
 * Nim: 12S24017
 */
public class App {

    private static EntityManagerFactory factory;
    private static EntityManager em;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory("park_IT");
        em = factory.createEntityManager();

        // Bersihkan data lama
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Vehicle").executeUpdate();
        em.createQuery("DELETE FROM ParkingArea").executeUpdate();
        em.getTransaction().commit();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                break;
            }

            String[] parts = input.split("#");
            String command = parts[0].trim();

            switch (command) {
                case "area-add":
                    handleAreaAdd(parts);
                    break;
                case "vehicle-add":
                    handleVehicleAdd(parts);
                    break;
                case "park":
                    handlePark(parts);
                    break;
                case "display-all":
                    handleDisplayAll();
                    break;
                default:
                    // abaikan perintah tidak dikenal
                    break;
            }
        }

        scanner.close();
        em.close();
        factory.close();
    }

    private static void handleAreaAdd(String[] parts) {
        // area-add#<name>#<capacity>#<allowed_type>
        String name = parts[1];
        int capacity = Integer.parseInt(parts[2]);
        String allowedType = parts[3];

        em.getTransaction().begin();
        if (em.find(ParkingArea.class, name) == null) {
            ParkingArea area = new ParkingArea(name, capacity, allowedType);
            em.persist(area);
        }
        em.getTransaction().commit();
    }

    private static void handleVehicleAdd(String[] parts) {
        // vehicle-add#<plate_number>#<owner>#<type>
        String plateNumber = parts[1];
        String owner = parts[2];
        String type = parts[3];

        em.getTransaction().begin();
        if (em.find(Vehicle.class, plateNumber) == null) {
            Vehicle vehicle = new Vehicle(plateNumber, owner, type);
            em.persist(vehicle);
        }
        em.getTransaction().commit();
    }

    private static void handlePark(String[] parts) {
        // park#<plate_number>#<area_name>
        String plateNumber = parts[1];
        String areaName = parts[2];

        em.getTransaction().begin();
        Vehicle vehicle = em.find(Vehicle.class, plateNumber);
        ParkingArea area = em.find(ParkingArea.class, areaName);

        if (vehicle != null && area != null) {
            // Validasi jenis kendaraan
            if (!vehicle.getType().equals(area.getAllowedType())) {
                em.getTransaction().rollback();
                return;
            }
            // Validasi kapasitas
            if (area.getVehicles().size() >= area.getCapacity()) {
                em.getTransaction().rollback();
                return;
            }
            // Kendaraan sudah diparkir di tempat lain, skip jika sudah ada
            if (vehicle.getParkingArea() != null) {
                em.getTransaction().rollback();
                return;
            }
            vehicle.setParkingArea(area);
            area.getVehicles().add(vehicle);
            em.merge(vehicle);
            em.getTransaction().commit();
        } else {
            em.getTransaction().rollback();
        }
    }

    private static void handleDisplayAll() {
        List<ParkingArea> areas = em.createQuery(
                "SELECT a FROM ParkingArea a ORDER BY a.name ASC", ParkingArea.class)
                .getResultList();

        for (ParkingArea area : areas) {
            System.out.println(area.toString());

            List<Vehicle> vehicles = area.getVehicles().stream()
                    .sorted(Comparator.comparing(Vehicle::getPlateNumber))
                    .collect(Collectors.toList());

            for (Vehicle v : vehicles) {
                System.out.println(v.toString());
            }
        }
    }
}
