package pbo.f01;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 12S24017 Arya Pratama Sinambela
 */

@Entity
@Table(name = "parking_area")
public class ParkingArea {

    @Id
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "allowed_type", length = 20, nullable = false)
    private String allowedType;

    @OneToMany(mappedBy = "parkingArea", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();

    public ParkingArea() {}

    public ParkingArea(String name, int capacity, String allowedType) {
        this.name = name;
        this.capacity = capacity;
        this.allowedType = allowedType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAllowedType() { return allowedType; }
    public void setAllowedType(String allowedType) { this.allowedType = allowedType; }

    public List<Vehicle> getVehicles() { return vehicles; }

    @Override
    public String toString() {
        return name + " " + allowedType + " " + capacity + "|" + vehicles.size();
    }
}
