package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Car;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSlotDetails
{
    private Car car;
    private String time;
    private int slotNumber;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public ParkingSlotDetails(Car car, int slotNumber)
    {
        this.car = car;
        this.slotNumber = slotNumber;
        this.time = LocalDateTime.now().format(format);
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
}