package com.bridgelabz.parkinglot.utility;

public class ParkingOwner implements Observer
{
    public String status;

    @Override
    public void parkingStatus(String status)
    {
        this.status = status;
    }
}