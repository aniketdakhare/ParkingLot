package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException
{
    public enum Type
    {
        CAR_NUMBER_MISMATCH("Car for a given car number is not found.");

        String massage;

        Type(String massage)
        {
            this.massage = massage;
        }
    }

    public Type type;

    public ParkingLotException(Type type)
    {
        super(type.massage);
        this.type = type;
    }
}