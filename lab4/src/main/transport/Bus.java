package main.transport;
import main.people.Human;

public class Bus extends Vehicle<Human> {
    public Bus(int maxSeats) {
        super(maxSeats);
    }
}