package com.bridgelabz.parkinglot.observer;

import com.bridgelabz.parkinglot.service.ParkingSlotDetails;

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

    public static int getSlotToPark(Map<Integer, ParkingSlotDetails> parkingMap)
    {
        return parkingMap.keySet()
                .stream()
                .filter(slot -> parkingMap.get(slot).getCarNumber() == null)
                .findFirst().orElse(0);
    }
}