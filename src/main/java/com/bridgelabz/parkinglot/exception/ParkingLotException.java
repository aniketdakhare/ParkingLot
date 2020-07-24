package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException
{
    public enum Type
    {
        CAR_NUMBER_MISMATCH("Car for a given car number is not found."),
        DUPLICATE_CAR("Car cannot be parked as given car is already present"),
        LOTS_ARE_FULL("All parking lots are full.");

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