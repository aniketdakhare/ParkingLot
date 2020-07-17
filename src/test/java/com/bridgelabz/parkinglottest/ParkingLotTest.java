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
}