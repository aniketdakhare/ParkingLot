package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Car;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSlotDetails
{
    private Car car;
    private String time;
    private int slotNumber;
    private int parkingLotNumber;
    private String attendantName;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public ParkingSlotDetails(Car car, int slotNumber, int parkingLotNumber, String attendantName)
    {
        this.car = car;
        this.time = LocalDateTime.now().format(format);
        this.slotNumber = slotNumber;
        this.parkingLotNumber = parkingLotNumber;
        this.attendantName =attendantName;
    }

    public ParkingSlotDetails() { }

    public String getParkedTime()
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