package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.CarType;
import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;
import com.bridgelabz.parkinglot.utility.ParkingSlotDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLot
{
    private Map<Integer, ParkingSlotDetails> parkingMap;
    private final int CAPACITY;
    private final String attendantName;
    private String parkingStatus;
    private int carCount = 0;
    private List<ParkingLotObserver> observerList;
    private ParkingSlotDetails slotDetails;

    public ParkingLot(int CAPACITY, String attendantName)
    {
        this.CAPACITY = CAPACITY;
        this.attendantName = attendantName;
        parkingMap = new LinkedHashMap<>();
        observerList = new ArrayList<>();
        slotDetails = new ParkingSlotDetails();
        IntStream.rangeClosed(1, (CAPACITY)).forEach(slotNumber -> parkingMap.put(slotNumber, slotDetails));
    }

    public void park(Car car, int parkingLotNumber)
    {
        if (carCount >= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_FULL.message;
            this.informObservers();
            return;
        }
        if (this.isCarPresent(car))
            throw new ParkingLotException(ParkingLotException.Type.DUPLICATE_CAR);
        int slotNumber = this.getSlotToPark(car.carType);
        this.parkingMap.put(slotNumber, new ParkingSlotDetails(car, slotNumber, parkingLotNumber, attendantName));
        carCount ++;
    }

    public boolean isCarPresent(Car car)
    {
        return parkingMap.values()
                .stream()
                .anyMatch(slot -> slot.getCar() == (car));
    }

    public void unPark(Car car)
    {
        int slot = this.carLocation(car);
        parkingMap.put(slot, slotDetails);
        if (parkingMap.size() <= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_IS_AVAILABLE.message;
            this.informObservers();
        }
        carCount --;
    }

    public int carLocation(Car car)
    {
        return this.getSlotDetails(car).getSlotNumber();
    }

    public List<ParkingSlotDetails> getCarsParkingDetailsBasedOnColour(String colour)
    {
        return this.parkingMap.values().stream().filter(parkingSlot -> parkingSlot.getCar() != null)
            .filter(parkingSlot -> parkingSlot.getCar().carColour.equalsIgnoreCase(colour))
            .collect(Collectors.toList());
    }

    public List<ParkingSlotDetails> getCarsParkingDetailsBasedOnCarCompany(String companyName)
    {
        return this.parkingMap.values().stream().filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> parkingSlot.getCar().carCompany.equalsIgnoreCase(companyName))
                .collect(Collectors.toList());
    }

    public List<ParkingSlotDetails> getParkingDetailsOfCarParkedWithInProvidedTime(int minutes)
    {
        return this.parkingMap.values().stream().filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> Duration.between(parkingSlot.getParkedTime(),
                        LocalDateTime.now()).toMinutes() <= minutes)
                .collect(Collectors.toList());
    }

    private ParkingSlotDetails getSlotDetails(Car car)
    {
        return this.parkingMap.values()
                .stream()
                .filter(slot -> car.equals(slot.getCar()))
                .findFirst()
                .orElseThrow(() -> new ParkingLotException(ParkingLotException.Type.CAR_DETAILS_MISMATCH));
    }

    public int getSlotToPark(CarType carType)
    {
        if (carType == CarType.LARGE_CAR)
            return this.parkingMap.keySet()
                    .stream()
                    .filter(slot -> parkingMap.get(slot).getCar() == null)
                    .filter(slot -> (slot + 1) <= CAPACITY && parkingMap.get(slot+1).getCar() == null)
                    .findFirst().orElse(0);
        return parkingMap.keySet()
                .stream()
                .filter(slot -> this.parkingMap.get(slot).getCar() == null)
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

    public LocalDateTime getParkedTime(Car car)
    {
        return this.getSlotDetails(car).getParkedTime();
    }

    public int getCarCount()
    {
        return carCount;
    }
}