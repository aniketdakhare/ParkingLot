package com.bridgelabz.parkinglot.service;

import java.util.Map;

public class ParkingAttendant
{
    private Map<Integer, String> parkingMap;

    public Map<Integer, String> parkCar(Map<Integer, String> parkingList, int slotNumber, String carNumber)
    {
        this.parkingMap = parkingList;
        this.parkingMap.put(slotNumber, carNumber);
        return this.parkingMap;
    }
}