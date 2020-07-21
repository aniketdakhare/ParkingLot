package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.observer.ParkingOwner;

import java.util.Map;

public class ParkingAttendant
{
    private Map<Integer, String> parkingMap;

    public Map<Integer, String> parkCar(Map<Integer, String> parkingMap, String carNumber)
    {
        int slotNumber = ParkingOwner.getSlotToPark(parkingMap);
        this.parkingMap = parkingMap;
        this.parkingMap.put(slotNumber, carNumber);
        return this.parkingMap;
    }
}