package main.transport;

import main.people.Human;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle<T extends Human> {

    protected final int maxSeats;
    protected final List<T> passengers = new ArrayList<>();

    public Vehicle(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public int getOccupiedSeats() {
        return passengers.size();
    }

    public void boardPassenger(T passenger) {
        if (passengers.size() >= maxSeats) {
            throw new IllegalStateException("Усі місця зайняті!");
        }
        passengers.add(passenger);
    }

    public void disembarkPassenger(T passenger) {
        if (!passengers.remove(passenger)) {
            throw new IllegalStateException("Такого пасажира немає в транспорті!");
        }
    }

    public List<T> getPassengers() {
        return passengers;
    }
}