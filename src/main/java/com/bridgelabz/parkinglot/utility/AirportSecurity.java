package com.bridgelabz.parkinglot.utility;

public class AirportSecurity implements Observer
{
    public String status;

    @Override
    public void parkingStatus(String status)
    {
        this.status = status;
    }
}