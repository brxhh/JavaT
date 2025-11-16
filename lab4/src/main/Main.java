package main;

import main.people.Firefighter;
import main.people.PoliceOfficer;
import main.people.RegularPassenger;
import main.road.Road;
import main.transport.Bus;
import main.transport.FireTruck;
import main.transport.PoliceCar;
import main.transport.Taxi;

public class Main {
    static void main() {

        Road road = new Road();

        Taxi taxi = new Taxi(4);
        taxi.boardPassenger(new RegularPassenger("Ivan"));
        taxi.boardPassenger(new PoliceOfficer("Petro"));

        Bus bus = new Bus(30);
        bus.boardPassenger(new RegularPassenger("Sergii"));
        bus.boardPassenger(new Firefighter("Andrii"));
        bus.boardPassenger(new PoliceOfficer("Oleh"));

        FireTruck fireTruck = new FireTruck(2);
        fireTruck.boardPassenger(new Firefighter("Oleksandr"));

        PoliceCar policeCar = new PoliceCar(2);
        policeCar.boardPassenger(new PoliceOfficer("Max"));

        road.addCarToRoad(taxi);
        road.addCarToRoad(bus);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        System.out.println("Загальна кількість людей на дорозі: " +
                road.getCountOfHumans());
    }
}