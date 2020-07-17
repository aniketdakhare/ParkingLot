package com.bridgelabz.parkinglottest;

import com.bridgelabz.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest
{
    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTrue()
    {
        ParkingLot parkingLot = new ParkingLot();
        boolean parked = parkingLot.park("MH-32-AW-4348");
        Assert.assertTrue(parked);
    }

    @Test
    public void givenCarDetails_WhenProvidedToUnParkTheCar_ShouldReturnTrue()
    {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.park("MH-32-AW-4348");
        boolean parked = parkingLot.unPark("MH-32-AW-4348");
        Assert.assertTrue(parked);
    }
}