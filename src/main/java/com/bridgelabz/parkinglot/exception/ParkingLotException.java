package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException
{
    public enum Type
    {
        CAR_NUMBER_MISMATCH("Car for a given car number is not found."),
        PARKING_FULL("Parking is full.");

        String message;

        Type(String message)
        {
            this.message = message;
        }
    }

    public Type type;

    public ParkingLotException(Type type)
    {
        super(type.message);
        this.type = type;
    }
}