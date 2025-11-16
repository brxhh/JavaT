package main.transport;
import main.people.PoliceOfficer;

public class PoliceCar extends Car<PoliceOfficer> {
    public PoliceCar(int maxSeats) {
        super(maxSeats);
    }
}