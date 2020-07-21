package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.Observer;
import com.bridgelabz.parkinglot.observer.ParkingOwner;

import java.util.HashMap;
import java.util.Map;

public class ParkingLot
{
    private Map<Integer, String> parkingMap;
    private final int CAPACITY;
    private String parkingStatus;
    private ParkingAttendant parkingAttendant;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
        parkingMap = new HashMap<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void park(int slotNumber, String carNumber)
    {
        if (parkingMap.containsValue(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        if (parkingMap.size() < CAPACITY)
        {
            this.parkingMap = parkingAttendant.parkCar(parkingMap, slotNumber, carNumber);
        }
        if (parkingMap.size() >= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_FULL.message;
            this.informOwner();
            this.informAirportSecurity();
        }
    }

    public boolean isCarPresent(String carNumber)
    {
        return parkingMap.containsValue(carNumber);
    }

    public void unPark(String carNumber)
    {
        if (!parkingMap.containsValue(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH);
        parkingMap.values().removeIf(value -> value.contains(carNumber));
        if (parkingMap.size() < CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_IS_AVAILABLE.message;
            this.informOwner();
        }
    }

    public int carLocation(String carNumber)
    {
        return parkingMap.keySet()
                .stream()
                .filter(key -> carNumber.equals(parkingMap.get(key)))
                .findFirst().get();
    }

    public String informOwner()
    {
        Observer owner = new ParkingOwner();
        owner.setParkingStatus(parkingStatus);
        return owner.getParkingStatus();
    }

    public String informAirportSecurity()
    {
        Observer airportSecurity = new AirportSecurity();
        airportSecurity.setParkingStatus(parkingStatus);
        return airportSecurity.getParkingStatus();
    }
}