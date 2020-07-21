package com.bridgelabz.parkinglot.enums;

public enum ParkingStatus
{
    PARKING_FULL("Parking lot is full."), PARKING_IS_AVAILABLE("Vacancy generated. Parking is available"),
    VACANT("Vacant");

    public String message;

    ParkingStatus(String message)
    {
        this.message = message;
    }
}
