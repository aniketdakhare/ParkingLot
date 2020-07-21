package com.bridgelabz.parkinglot.observer;

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
}