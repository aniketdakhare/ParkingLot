package com.bridgelabz.parkinglot.observer;

import com.bridgelabz.parkinglot.enums.ParkingStatus;

import java.util.Map;
import java.util.Objects;

public class ParkingOwner implements ParkingLotObserver
{
    private String parkingStatus;

    @Override
    public String getParkingStatus()
    {
        return parkingStatus;
    }

    @Override
    public void setParkingStatus(String parkingStatus)
    {
        this.parkingStatus = parkingStatus;
    }

    public static int getSlotToPark(Map<Integer, String> parkingMap)
    {
        return parkingMap.keySet()
                .stream()
                .filter(key -> Objects.equals(ParkingStatus.VACANT.message, parkingMap.get(key)))
                .findFirst().orElse(0);
    }
}