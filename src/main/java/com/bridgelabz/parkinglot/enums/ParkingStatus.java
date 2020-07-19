package com.bridgelabz.parkinglot.enums;

public enum ParkingStatus
{
    PARKING_FULL("Parking lot is full.");

    public String message;

    ParkingStatus(String message)
    {
        this.message = message;
    }
}
