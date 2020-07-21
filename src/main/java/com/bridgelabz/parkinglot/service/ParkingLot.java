package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;
import com.bridgelabz.parkinglot.observer.ParkingOwner;

import java.util.*;

public class ParkingLot
{
    private Map<Integer, String> parkingMap;
    private final int CAPACITY;
    private String parkingStatus;
    private List<ParkingLotObserver> observerList;
    private ParkingAttendant parkingAttendant;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
        parkingMap = new HashMap<>();
        observerList = new ArrayList<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void park(int slotNumber, String carNumber)
    {
        if (parkingMap.containsValue(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        if (parkingMap.size() < CAPACITY)
            this.parkingMap = parkingAttendant.parkCar(parkingMap, slotNumber, carNumber);
        if (parkingMap.size() >= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_FULL.message;
            this.informObservers();
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
            this.informObservers();
        }
    }

    public int carLocation(String carNumber)
    {
        if (!parkingMap.containsValue(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH);
        return parkingMap.keySet()
                .stream()
                .filter(key -> carNumber.equals(parkingMap.get(key)))
                .findFirst().get();
    }

    public void informObservers()
    {
        observerList.forEach(observer -> observer.setParkingStatus(parkingStatus));
    }

    public void addParkingLotObservers(ParkingLotObserver... observer)
    {
        Collections.addAll(observerList, observer);
    }
}