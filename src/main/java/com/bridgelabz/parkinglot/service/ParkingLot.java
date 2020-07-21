package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.util.*;
import java.util.stream.IntStream;

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
        parkingMap = new LinkedHashMap<>();
        observerList = new ArrayList<>();
        parkingAttendant = new ParkingAttendant();
        IntStream.rangeClosed(1, (CAPACITY)).forEach(slotNumber -> parkingMap.put(slotNumber, ParkingStatus.VACANT.message));
    }

    public void park(String carNumber)
    {
        if (parkingMap.containsValue(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        this.parkingMap = parkingAttendant.parkCar(parkingMap, carNumber);
        if (parkingMap.size() > CAPACITY)
        {
            parkingMap.values().removeIf(value -> value.contains(carNumber));
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
        int slot = this.carLocation(carNumber);
        parkingMap.put(slot, ParkingStatus.VACANT.message);
        if (parkingMap.size() <= CAPACITY)
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