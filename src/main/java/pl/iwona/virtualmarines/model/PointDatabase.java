package pl.iwona.virtualmarines.model;


import javax.persistence.*;

@Entity
public class PointDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private double y;
    private double x;
    private String name;
    @Column(name="destination_x")
    private double destinationX;
    @Column(name="destination_Y")
    private double destinationY;

    public PointDatabase() {
    }

    public PointDatabase(double y, double x, String name, double destinationX, double destinationY) {
        this.y = y;
        this.x = x;
        this.name = name;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(double destinationY) {
        this.destinationY = destinationY;
    }

    public double getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(double destinationX) {
        this.destinationX = destinationX;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

}
