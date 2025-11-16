package main.road;

import main.people.Human;
import main.transport.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Road {

    private final List<Vehicle<? extends Human>> carsInRoad = new ArrayList<>();

    public void addCarToRoad(Vehicle<? extends Human> car) {
        carsInRoad.add(car);
    }

    public int getCountOfHumans() {
        int total = 0;
        for (Vehicle<? extends Human> v : carsInRoad) {
            total += v.getOccupiedSeats();
        }
        return total;
    }
}