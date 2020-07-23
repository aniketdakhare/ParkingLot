package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.util.*;
import java.util.stream.IntStream;

public class ParkingLot
{
    private Map<Integer, ParkingSlotDetails> parkingMap;
    private final int CAPACITY;
    private String parkingStatus;
    private int carCount = 0;
    private List<ParkingLotObserver> observerList;
    private ParkingSlotDetails slotDetails;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
        parkingMap = new LinkedHashMap<>();
        observerList = new ArrayList<>();
        slotDetails = new ParkingSlotDetails();
        IntStream.rangeClosed(1, (CAPACITY)).forEach(slotNumber -> parkingMap.put(slotNumber, slotDetails));
    }

    public void park(String carNumber)
    {
        if (carCount >= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_FULL.message;
            this.informObservers();
            return;
        }
        if (this.isCarPresent(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        int slotNumber = this.getSlotToPark(this.parkingMap);
        this.parkingMap.put(slotNumber, new ParkingSlotDetails(carNumber, slotNumber));
        carCount ++;
    }

    public boolean isCarPresent(String carNumber)
    {
        return parkingMap.values()
                .stream()
                .anyMatch(value -> value.getCarNumber() == (carNumber));
    }

    public void unPark(String carNumber)
    {
        int slot = this.carLocation(carNumber);
        parkingMap.put(slot, slotDetails);
        if (parkingMap.size() <= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_IS_AVAILABLE.message;
            this.informObservers();
        }
        carCount --;
    }

    public int carLocation(String carNumber)
    {
        return this.getSlotDetails(carNumber).getSlotNumber();
    }

    private ParkingSlotDetails getSlotDetails(String carNumber)
    {
        return this.parkingMap.values()
                .stream()
                .filter(slot -> carNumber.equals(slot.getCarNumber()))
                .findFirst()
                .orElseThrow(() -> new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH));
    }

    public int getSlotToPark(Map<Integer, ParkingSlotDetails> parkingMap)
    {
        return parkingMap.keySet()
                .stream()
                .filter(slot -> parkingMap.get(slot).getCarNumber() == null)
                .findFirst().orElse(0);
    }

    public void informObservers()
    {
        observerList.forEach(observer -> observer.setParkingStatus(parkingStatus));
    }

    public void addParkingLotObservers(ParkingLotObserver... observer)
    {
        Collections.addAll(observerList, observer);
    }

    public String getParkedTime(String carNumber)
    {
        return this.getSlotDetails(carNumber).getParkedTime();
    }

    public int getCarCount()
    {
        return carCount;
    }
}