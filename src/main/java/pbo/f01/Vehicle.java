package pbo.f01;

import jakarta.persistence.*;

/**
 * @author 12S24017 Arya Pratama Sinambela
 */

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "plate_number", length = 20, nullable = false)
    private String plateNumber;

    @Column(name = "owner", length = 100, nullable = false)
    private String owner;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "parking_area_name")
    private ParkingArea parkingArea;

    public Vehicle() {}

    public Vehicle(String plateNumber, String owner, String type) {
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.type = type;
    }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public ParkingArea getParkingArea() { return parkingArea; }
    public void setParkingArea(ParkingArea parkingArea) { this.parkingArea = parkingArea; }

    @Override
    public String toString() {
        return plateNumber + " " + owner + " " + type;
    }
}
