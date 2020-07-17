package com.bridgelabz.parkinglot.service;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot
{
    private List<String> parkingList = new ArrayList<>();

    public boolean park( String carNumber)
    {
        parkingList.add(carNumber);
        return parkingList.contains(carNumber);
    }

    public boolean unPark(String carNumber)
    {
        parkingList.remove(carNumber);
        return !parkingList.contains(carNumber);
    }
}