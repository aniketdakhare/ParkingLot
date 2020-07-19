package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.utility.AirportSecurity;
import com.bridgelabz.parkinglot.utility.ParkingOwner;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot
{
    private List<String> parkingList = new ArrayList<>();
    private final int CAPACITY;
    private String parkingStatus;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
    }

    public void park(String carNumber)
    {
        if (parkingList.contains(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        if (parkingList.size() < CAPACITY)
            parkingList.add(carNumber);
        if (parkingList.size() >= CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_FULL.message;
            this.informOwner();
            this.informAirportSecurity();
        }
    }

    public boolean isCarPresent(String carNumber)
    {
        return parkingList.contains(carNumber);
    }

    public void unPark(String carNumber)
    {
        if (!parkingList.contains(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH);
        parkingList.remove(carNumber);
    }

    public String informOwner()
    {
        ParkingOwner owner = new ParkingOwner();
        owner.parkingStatus(parkingStatus);
        return owner.status;
    }

    public String informAirportSecurity()
    {
        AirportSecurity airportSecurity = new AirportSecurity();
        airportSecurity.parkingStatus(parkingStatus);
        return airportSecurity.status;
    }
}