package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.Observer;
import com.bridgelabz.parkinglot.observer.ParkingOwner;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot
{
    private List<String> parkingList;
    private final int CAPACITY;
    private String parkingStatus;
    private ParkingAttendant parkingAttendant;

    public ParkingLot(int CAPACITY)
    {
        this.CAPACITY = CAPACITY;
        parkingList = new ArrayList<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void park(String carNumber)
    {
        if (parkingList.contains(carNumber))
            throw new ParkingLotException(ParkingLotException.Type.SAME_CAR_NUMBER);
        if (parkingList.size() < CAPACITY)
        {
            this.parkingList = parkingAttendant.parkCar(parkingList, carNumber);
        }
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
        if (parkingList.size() < CAPACITY)
        {
            this.parkingStatus = ParkingStatus.PARKING_IS_AVAILABLE.message;
            this.informOwner();
        }
    }

    public String informOwner()
    {
        Observer owner = new ParkingOwner();
        owner.setParkingStatus(parkingStatus);
        return owner.getParkingStatus();
    }

    public String informAirportSecurity()
    {
        Observer airportSecurity = new AirportSecurity();
        airportSecurity.setParkingStatus(parkingStatus);
        return airportSecurity.getParkingStatus();
    }
}