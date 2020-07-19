package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.utility.AirportSecurity;
import com.bridgelabz.parkinglot.utility.ParkingOwner;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot
{
    private List<String> parkingList = new ArrayList<>();
    private final int CAPACITY;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
    }

    public boolean park(String carNumber)
    {
        if (parkingList.size() >= CAPACITY)
            return false;
        parkingList.add(carNumber);
        return parkingList.contains(carNumber);
    }

    public boolean unPark(String carNumber)
    {
        if (!parkingList.contains(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH);
        parkingList.remove(carNumber);
        return !parkingList.contains(carNumber);
    }

    public String informOwner(String carNumber)
    {
        return new ParkingOwner().isParkingFull(this.park(carNumber));
    }

    public String informAirportSecurity(String carNumber)
    {
        return new AirportSecurity().isParkingFull(this.park(carNumber));
    }
}