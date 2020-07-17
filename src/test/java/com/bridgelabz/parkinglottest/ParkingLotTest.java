package com.bridgelabz.parkinglottest;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest
{
    ParkingLot parkingLot;

    @Before
    public void setUp()
    {
        parkingLot = new ParkingLot();
    }

    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTrue()
    {
        boolean parked = parkingLot.park("MH-32-AW-4348");
        Assert.assertTrue(parked);
    }

    @Test
    public void givenCarDetails_WhenProvidedToUnParkTheCar_ShouldReturnTrue()
    {
        parkingLot.park("MH-32-AW-4348");
        boolean parked = parkingLot.unPark("MH-32-AW-4348");
        Assert.assertTrue(parked);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToUnParkTheCar_ShouldGiveException()
    {
        parkingLot.park("MH-32-AW-4348");
        parkingLot.unPark("MH-34-AW-7555");
    }
}