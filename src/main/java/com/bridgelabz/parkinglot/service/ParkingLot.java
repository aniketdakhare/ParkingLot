package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot
{
    private List<String> parkingList = new ArrayList<>();

    public boolean park( String carNumber)
    {
        int CAPACITY = 3;
        if (parkingList.size() >= CAPACITY)
            throw new ParkingLotException(ParkingLotException.Type.PARKING_FULL);
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
}