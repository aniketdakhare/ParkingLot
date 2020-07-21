package com.bridgelabz.parkinglottest;

import com.bridgelabz.parkinglot.enums.ParkingStatus;
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
        parkingLot = new ParkingLot(3);
    }

    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTrue()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenSameCarDetailsTwice_WhenProvidedToParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park(1, "MH-32-AW-4348");
            parkingLot.park(2, "MH-32-AW-4348");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.SAME_CAR_NUMBER, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenProvidedToUnParkTheCar_ShouldReturnTrue()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        parkingLot.unPark("MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertFalse(isPresent);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToUnParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park(1, "MH-32-AW-4348");
            parkingLot.unPark("MH-34-AW-7555");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.CAR_NUMBER_MISMATCH, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenTheParkingIsFull_ShouldInformToOwner()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        parkingLot.park(2, "MH-22-RT-2324");
        parkingLot.park(3, "MH-26-YU-8884");
        String status = parkingLot.informOwner();
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, status);
    }

    @Test
    public void givenCarDetails_WhenTheParkingIsFull_ShouldInformToAirportSecurity()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        parkingLot.park(2, "MH-22-RT-2324");
        parkingLot.park(3, "MH-26-YU-8884");
        String status = parkingLot.informAirportSecurity();
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, status);
    }

    @Test
    public void givenParkingLotCapacity_WhenAvailable_ShouldInformToOwner()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        parkingLot.park(2, "MH-22-RT-2324");
        parkingLot.park(3, "MH-26-YU-8884");
        parkingLot.unPark("MH-22-RT-2324");
        String status = parkingLot.informOwner();
        Assert.assertEquals(ParkingStatus.PARKING_IS_AVAILABLE.message, status);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedSlot_ShouldReturnTrue()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenCarDetails_WhenProvidedToGetCarLocation_ShouldReturnParkingSlotNumber()
    {
        parkingLot.park(1, "MH-32-AW-4348");
        parkingLot.park(2, "MH-22-RT-2324");
        parkingLot.park(3, "MH-26-YU-8884");
        int slotNumber = parkingLot.carLocation("MH-22-RT-2324");
        Assert.assertEquals(2, slotNumber);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToGetCarLocation_ShouldGiveException()
    {
        try
        {
            parkingLot.park(1, "MH-32-AW-4348");
            parkingLot.park(2, "MH-22-RT-2324");
            parkingLot.park(3, "MH-26-YU-8884");
            parkingLot.carLocation("MH-22-RT-2325");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.CAR_NUMBER_MISMATCH, e.type);
        }
    }
}