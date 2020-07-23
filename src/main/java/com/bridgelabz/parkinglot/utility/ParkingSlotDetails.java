package com.bridgelabz.parkinglot.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSlotDetails
{
    private  String carNumber;
    private String time;
    private int slotNumber;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public ParkingSlotDetails(String carNumber, int slotNumber)
    {
        this.carNumber = carNumber;
        this.slotNumber = slotNumber;
        this.time = LocalDateTime.now().format(format);
    }

    public ParkingSlotDetails() { }

    public String getParkedTime()
    {
        return time;
    }

    public String getCarNumber()
    {
        return carNumber;
    }

    public int getSlotNumber()
    {
        return slotNumber;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return carNumber.equals(that.carNumber) &&
                time.equals(that.time);
    }
}