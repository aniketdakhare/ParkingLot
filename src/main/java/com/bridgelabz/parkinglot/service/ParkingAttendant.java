package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.observer.ParkingOwner;

import java.util.Map;

public class ParkingAttendant
{
    private Map<Integer, ParkingSlotDetails> parkingMap;

    public Map<Integer, ParkingSlotDetails> parkCar(Map<Integer, ParkingSlotDetails> parkingMap, String carNumber)
    {
        int slotNumber = ParkingOwner.getSlotToPark(parkingMap);
        this.parkingMap = parkingMap;
        this.parkingMap.put(slotNumber, new ParkingSlotDetails(carNumber, slotNumber));
        return this.parkingMap;
    }
}