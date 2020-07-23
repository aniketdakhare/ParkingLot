package com.bridgelabz.parkinglottest;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;
import com.bridgelabz.parkinglot.observer.ParkingOwner;
import com.bridgelabz.parkinglot.service.ParkingService;
import com.bridgelabz.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        parkingLot.park("MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenSameCarDetailsTwice_WhenProvidedToParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park("MH-32-AW-4348");
            parkingLot.park("MH-32-AW-4348");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.SAME_CAR_NUMBER, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenProvidedToUnParkTheCar_ShouldReturnFalse()
    {
        parkingLot.park("MH-32-AW-4348");
        parkingLot.unPark("MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertFalse(isPresent);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToUnParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park("MH-32-AW-4348");
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
        ParkingLotObserver owner = new ParkingOwner();
        parkingLot.addParkingLotObservers(owner);
        parkingLot.park("MH-32-AW-4348");
        parkingLot.park("MH-22-RT-2324");
        parkingLot.park("MH-26-YU-8884");
        parkingLot.park("MH-26-YU-4444");
        String status = owner.getParkingStatus();
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, status);
    }

    @Test
    public void givenCarDetails_WhenTheParkingIsFull_ShouldInformToAirportSecurity()
    {
        ParkingLotObserver owner = new ParkingOwner();
        ParkingLotObserver airportSecurity = new AirportSecurity();
        parkingLot.addParkingLotObservers(owner, airportSecurity);
        parkingLot.park("MH-32-AW-4348");
        parkingLot.park("MH-22-RT-2324");
        parkingLot.park("MH-26-YU-8884");
        parkingLot.park("MH-26-YU-4444");
        String ownerStatus = owner.getParkingStatus();
        String airportSecurityStatus = airportSecurity.getParkingStatus();
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, airportSecurityStatus);
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, ownerStatus);
    }

    @Test
    public void givenParkingLotCapacity_WhenAvailable_ShouldInformToOwner()
    {
        ParkingLotObserver owner = new ParkingOwner();
        parkingLot.addParkingLotObservers(owner);
        parkingLot.park("MH-32-AW-4348");
        parkingLot.park("MH-22-RT-2324");
        parkingLot.park("MH-26-YU-8884");
        parkingLot.unPark("MH-22-RT-2324");
        String status = owner.getParkingStatus();
        Assert.assertEquals(ParkingStatus.PARKING_IS_AVAILABLE.message, status);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedSlot_ShouldReturnTrue()
    {
        parkingLot.park("MH-32-AW-4348");
        boolean isPresent = parkingLot.isCarPresent("MH-32-AW-4348");
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenCarDetails_WhenProvidedToGetCarLocation_ShouldReturnParkingSlotNumber()
    {
        parkingLot.park("MH-32-AW-4348");
        parkingLot.park("MH-22-RT-2324");
        int slotNumber = parkingLot.carLocation("MH-22-RT-2324");
        Assert.assertEquals(2, slotNumber);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToGetCarLocation_ShouldGiveException()
    {
        try
        {
            parkingLot.park("MH-32-AW-4348");
            parkingLot.park("MH-22-RT-2324");
            parkingLot.park("MH-26-YU-8884");
            parkingLot.carLocation("MH-22-RT-2325");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.CAR_NUMBER_MISMATCH, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTimeOfParking()
    {
        parkingLot.park("MH-32-AW-4348");
        String parkedTime = parkingLot.getParkedTime("MH-32-AW-4348");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
        Assert.assertEquals(LocalDateTime.now().format(format), parkedTime);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedLotAndSlot_ShouldReturnTrue()
    {
        ParkingService parkingService = new ParkingService(5, 3);
        parkingService.parkCar("MH-32-AW-4348", DriverType.NORMAL_DRIVER);
        boolean isPresent = parkingService.isCarPresent("MH-32-AW-4348");
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedLotAndSlot_ShouldReturnLocationOfCar()
    {
        ParkingService parkingService = new ParkingService(5, 3);
        parkingService.parkCar("MH-22-RT-2324", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-26-YU-8884", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-14-OP-2222", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-33-KL-5454", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-35-SD-3333", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-32-AW-4348", DriverType.NORMAL_DRIVER);
        String carLocation = parkingService.getCarLocation("MH-32-AW-4348");
        String expectedLocation = "Parking Lot: 3  Parking Slot: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenAllParkingLotsAreFull_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(2, 2);
            parkingService.parkCar("MH-22-RT-2324", DriverType.NORMAL_DRIVER);
            parkingService.parkCar("MH-26-YU-8884", DriverType.NORMAL_DRIVER);
            parkingService.parkCar("MH-14-OP-2222", DriverType.NORMAL_DRIVER);
            parkingService.parkCar("MH-33-KL-5454", DriverType.NORMAL_DRIVER);
            parkingService.parkCar("MH-35-SD-3333", DriverType.NORMAL_DRIVER);
            parkingService.parkCar("MH-32-AW-4348", DriverType.NORMAL_DRIVER);
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.LOTS_ARE_FULL, e.type);
        }
    }

    @Test
    public void givenCarDetailsOfHandicapDriver_WhenParkedAtNearestLotWithFreeSpace_ShouldReturnLocationOfCar()
    {
        ParkingService parkingService = new ParkingService(3, 3);
        parkingService.parkCar("MH-22-RT-2324", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-26-YU-8884", DriverType.HANDICAP_DRIVER);
        parkingService.parkCar("MH-14-OP-2222", DriverType.HANDICAP_DRIVER);
        parkingService.parkCar("MH-18-OC-9922", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-33-KL-5454", DriverType.HANDICAP_DRIVER);
        parkingService.parkCar("MH-35-SD-3333", DriverType.NORMAL_DRIVER);
        parkingService.parkCar("MH-32-AW-4348", DriverType.HANDICAP_DRIVER);
        String carLocation = parkingService.getCarLocation("MH-33-KL-5454");
        String expectedLocation = "Parking Lot: 2  Parking Slot: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }
}