package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException
{
    public enum Type
    {
        CAR_NUMBER_MISMATCH("Car for a given car number is not found."),
        SAME_CAR_NUMBER("Car cannot be parked as car number is same"),
        LOTS_ARE_FULL("All parking lots are full");

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