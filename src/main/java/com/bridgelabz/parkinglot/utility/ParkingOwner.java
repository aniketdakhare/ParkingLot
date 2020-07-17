package com.bridgelabz.parkinglot.utility;

public class ParkingOwner implements Observer
{
    @Override
    public String isParkingFull(boolean check)
    {
        if (!check)
            return "Parking is Full";
        return "Parking is Available";
    }
}