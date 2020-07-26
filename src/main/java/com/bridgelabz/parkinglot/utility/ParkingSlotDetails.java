package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Car;

import java.time.LocalDateTime;

public class ParkingSlotDetails
{
    private Car car;
    private LocalDateTime time;
    private int slotNumber;
    private int parkingLotNumber;
    private String attendantName;

    public ParkingSlotDetails(Car car, int slotNumber, int parkingLotNumber, String attendantName)
    {
        this.car = car;
        this.time = LocalDateTime.now().withNano(0);
        this.slotNumber = slotNumber;
        this.parkingLotNumber = parkingLotNumber;
        this.attendantName =attendantName;
    }

    public ParkingSlotDetails() { }

    public LocalDateTime getParkedTime()
    {
        return time;
    }

    public Car getCar()
    {
        return car;
    }

    public int getSlotNumber()
    {
        return slotNumber;
    }

    public int getParkingLotNumber()
    {
        return parkingLotNumber;
    }

    public String getAttendantName()
    {
        return attendantName;
    }
}