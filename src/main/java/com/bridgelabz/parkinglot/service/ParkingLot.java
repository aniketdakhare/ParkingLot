package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.utility.AirportSecurity;
import com.bridgelabz.parkinglot.utility.Observer;
import com.bridgelabz.parkinglot.utility.ParkingOwner;

import java.security.acl.Owner;
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

    public void park(String carNumber)
    {
        if (parkingList.size() >= CAPACITY)
        {
            this.informOwner(ParkingStatus.PARKING_FULL.message);
            this.informAirportSecurity(ParkingStatus.PARKING_FULL.message);
            return;
        }
        parkingList.add(carNumber);
    }

    public boolean isCarPresent(String carNumber)
    {
        return parkingList.contains(carNumber);
    }

    public boolean unPark(String carNumber)
    {
        if (!parkingList.contains(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.CAR_NUMBER_MISMATCH);
        parkingList.remove(carNumber);
        return !parkingList.contains(carNumber);
    }

    public String informOwner(String status)
    {
        ParkingOwner owner = new ParkingOwner();
        owner.parkingStatus(status);
        return owner.status;
    }

    public String informAirportSecurity(String status)
    {
        AirportSecurity airportSecurity = new AirportSecurity();
        airportSecurity.parkingStatus(status);
        return airportSecurity.status;
    }
}