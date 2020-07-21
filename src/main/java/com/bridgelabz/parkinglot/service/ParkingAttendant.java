package com.bridgelabz.parkinglot.service;

import java.util.List;

public class ParkingAttendant
{
    private List<String> parkingList;

    public List<String> parkCar(List<String> parkingList, String carNumber)
    {
        this.parkingList = parkingList;
        this.parkingList.add(carNumber);
        return this.parkingList;
    }
}