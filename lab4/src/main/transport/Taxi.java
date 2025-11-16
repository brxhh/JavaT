package main.transport;
import main.people.Human;

public class Taxi extends Car<Human> {
    public Taxi(int maxSeats) {
        super(maxSeats);
    }
}