package com.bridgelabz.parkinglot.utility;

public class AirportSecurity implements Observer
{
    @Override
    public String isParkingFull(boolean check)
    {
        if (!check)
            return "Redirect to Another parking";
        return "Parking Available";
    }
}