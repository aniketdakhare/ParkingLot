package com.bridgelabz.parkinglottest;

import com.bridgelabz.parkinglot.enums.CarType;
import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.ParkingStatus;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
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
import java.util.Arrays;
import java.util.List;

public class ParkingLotTest
{
    ParkingLot parkingLot;
    Car car;
    Car firstCar;
    Car secondCar;
    Car thirdCar;
    String[] attendantNames;

    @Before
    public void setUp()
    {
        attendantNames = new String[]{"FirstAttendant", "SecondAttendant", "ThirdAttendant"};
        parkingLot = new ParkingLot(3, "Aniket");
        car = new Car();
        firstCar = new Car("MH-32-AW-4348", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR);
        secondCar = new Car("MH-33-KL-5454", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR);
        thirdCar = new Car("MH-32-AW-4348", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "WHITE");
    }

    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTrue()
    {
        parkingLot.park(car, 1);
        boolean isPresent = parkingLot.isCarPresent(car);
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenSameCarDetailsTwice_WhenProvidedToParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park(car, 1);
            parkingLot.park(car, 1);
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.DUPLICATE_CAR, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenProvidedToUnParkTheCar_ShouldReturnFalse()
    {
        parkingLot.park(car, 1);
        parkingLot.unPark(car);
        boolean isPresent = parkingLot.isCarPresent(car);
        Assert.assertFalse(isPresent);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToUnParkTheCar_ShouldGiveException()
    {
        try
        {
            parkingLot.park(car, 1);
            parkingLot.unPark(new Car());
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.CAR_DETAILS_MISMATCH, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenTheParkingIsFull_ShouldInformToOwner()
    {
        ParkingLotObserver owner = new ParkingOwner();
        parkingLot.addParkingLotObservers(owner);
        parkingLot.park(car, 1);
        parkingLot.park(new Car(), 1);
        parkingLot.park(new Car(), 1);
        parkingLot.park(new Car(), 1);
        String status = owner.getParkingStatus();
        Assert.assertEquals(ParkingStatus.PARKING_FULL.message, status);
    }

    @Test
    public void givenCarDetails_WhenTheParkingIsFull_ShouldInformToAirportSecurity()
    {
        ParkingLotObserver owner = new ParkingOwner();
        ParkingLotObserver airportSecurity = new AirportSecurity();
        parkingLot.addParkingLotObservers(owner, airportSecurity);
        parkingLot.park(car, 1);
        parkingLot.park(new Car(), 1);
        parkingLot.park(new Car(), 1);
        parkingLot.park(new Car(), 1);
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
        parkingLot.park(car, 1);
        parkingLot.park(new Car(), 1);
        parkingLot.park(new Car(), 1);
        parkingLot.unPark(car);
        String status = owner.getParkingStatus();
        Assert.assertEquals(ParkingStatus.PARKING_IS_AVAILABLE.message, status);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedSlot_ShouldReturnTrue()
    {
        parkingLot.park(car, 1);
        boolean isPresent = parkingLot.isCarPresent(car);
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenCarDetails_WhenProvidedToGetCarLocation_ShouldReturnParkingSlotNumber()
    {
        parkingLot.park(car, 1);
        parkingLot.park(new Car(), 1);
        int slotNumber = parkingLot.carLocation(car);
        Assert.assertEquals(1, slotNumber);
    }

    @Test
    public void givenIncorrectCarDetails_WhenProvidedToGetCarLocation_ShouldGiveException()
    {
        try
        {
            parkingLot.park(car, 1);
            parkingLot.park(new Car(), 1);
            parkingLot.park(new Car(), 1);
            parkingLot.carLocation(new Car());
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.CAR_DETAILS_MISMATCH, e.type);
        }
    }

    @Test
    public void givenCarDetails_WhenAddedToParkingLot_ShouldReturnTimeOfParking()
    {
        parkingLot.park(car, 1);
        LocalDateTime parkedTime = parkingLot.getParkedTime(car);
        Assert.assertEquals(LocalDateTime.now().withNano(0), parkedTime);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedLotAndSlot_ShouldReturnTrue()
    {
        ParkingService parkingService = new ParkingService(5, 3, attendantNames);
        parkingService.parkCar(firstCar);
        boolean isPresent = parkingService.isCarPresent(firstCar);
        Assert.assertTrue(isPresent);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenParkedAsPerProvidedLotAndSlot_ShouldReturnLocationOfCar()
    {
        ParkingService parkingService = new ParkingService(5, 3, attendantNames);
        Car car1 = new Car("MH-26-YU-8884", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR);
        parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(car1);
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-33-KL-5454", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.unParkCar(car1);
        parkingService.parkCar(new Car("MH-35-SD-3333", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(firstCar);
        String carLocation = parkingService.getCarLocation(firstCar);
        String expectedLocation = "Parking Lot: 2  Parking Slot: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCarDetailsToParkingAttendant_WhenAllParkingLotsAreFull_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(2, 2, attendantNames);
            parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
            parkingService.parkCar(new Car("MH-26-YU-8884", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
            parkingService.parkCar(new Car("MH-33-KL-5454", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
            parkingService.parkCar(new Car("MH-35-SD-3333", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
            parkingService.parkCar(firstCar);
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.LOTS_ARE_FULL, e.type);
        }
    }

    @Test
    public void givenSameCarDetailsTwiceToParkingAttendant_WhenTriedToGetParked_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(2, 2, attendantNames);
            parkingService.parkCar(firstCar);
            parkingService.parkCar(firstCar);
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.DUPLICATE_CAR, e.type);
        }
    }

    @Test
    public void givenCarDetailsOfHandicapDriver_WhenParkedAtNearestLotWithFreeSpace_ShouldReturnLocationOfCar()
    {
        ParkingService parkingService = new ParkingService(3, 3, attendantNames);
        parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(secondCar);
        parkingService.parkCar(new Car("MH-35-SD-3333", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(firstCar);
        String carLocation = parkingService.getCarLocation(secondCar);
        String expectedLocation = "Parking Lot: 2  Parking Slot: 2";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenCarDetailsOfLargeVehicle_WhenParkedAtLotWithHighestNumberOfFreeSpace_ShouldReturnLocationOfCar()
    {
        Car forthCar = new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR);
        Car fifthCar = new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR);
        ParkingService parkingService = new ParkingService(6, 3, attendantNames);
        parkingService.parkCar(forthCar);
        parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-22-RT-2323", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-22-RT-2304", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(fifthCar);
        parkingService.parkCar(new Car("MH-22-RT-2325", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-22-RT-2328", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-22-RT-2329", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR));
        parkingService.parkCar(new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.LARGE_CAR));
        parkingService.parkCar(secondCar);
        parkingService.parkCar(new Car("MH-22-RT-2394", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.parkCar(new Car("MH-22-RT-2004", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR));
        parkingService.unParkCar(forthCar);
        parkingService.unParkCar(fifthCar);
        parkingService.parkCar(thirdCar);
        String carLocation = parkingService.getCarLocation(thirdCar);
        String expectedLocation = "Parking Lot: 3  Parking Slot: 5";
        Assert.assertEquals(expectedLocation, carLocation);
    }

    @Test
    public void givenColourOfTheCar_WhenSearchedInAllParkingLots_ShouldReturnListOfLocationsOfCar()
    {
        ParkingService parkingService = new ParkingService(6, 3, attendantNames);
        Car firstWhiteCar = new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR, "WHITE");
        Car secondWhiteCar = new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.LARGE_CAR, "WHITE");
        parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED"));
        parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK"));
        parkingService.parkCar(firstWhiteCar);
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE"));
        parkingService.parkCar(secondWhiteCar);
        parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW"));
        parkingService.parkCar(thirdCar);
        List<String> whiteCarsLocationList = parkingService.getLocationOfCarBasedOnColour("WHITE");
        List<String> expectedListOfLocation = Arrays.asList(parkingService.getCarLocation(firstWhiteCar),
                parkingService.getCarLocation(thirdCar), parkingService.getCarLocation(secondWhiteCar));
       Assert.assertEquals(expectedListOfLocation, whiteCarsLocationList);
    }

    @Test
    public void givenColourOfTheCar_WhenNotPresentInAnyOfTheParkingLot_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(6, 3, attendantNames);
            parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED", "TOYOTA"));
            parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK" , "TOYOTA"));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "BMW"));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "TATA"));
            parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW", "TATA"));
            parkingService.getLocationOfCarBasedOnColour("WHITE");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.NO_SUCH_CAR_PRESENT, e.type);
        }
    }

    @Test
    public void givenColourAndCompanyNameOfTheCar_WhenSearchedInAllParkingLots_ShouldReturnListOfLocationsPlateNumberAndAttendantNameOfCar()
    {
        ParkingService parkingService = new ParkingService(6, 3, attendantNames);
        Car firstBlueToyotaCar = new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR, "BLUE", "TOYOTA");
        Car secondBlueToyotaCar = new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.LARGE_CAR, "BLUE", "TOYOTA");
        parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED", "TOYOTA"));
        parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK" , "TOYOTA"));
        parkingService.parkCar(firstBlueToyotaCar);
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "BMW"));
        parkingService.parkCar(secondBlueToyotaCar);
        parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW", "TATA"));
        List<String> carsLocationList = parkingService.getParkingDetailsOfCarBasedOnColourAndCompany("BLUE", "TOYOTA");
        List<String> expectedListOfLocation = Arrays.asList("( Parking Lot: 1, Parking Slot: 2," +
                        " Plate Number: MH-26-YU-8884, Attendant Name: FirstAttendant )",
                "( Parking Lot: 2, Parking Slot: 2, Plate Number: MH-18-OC-9922, Attendant Name: SecondAttendant )");
        Assert.assertEquals(expectedListOfLocation, carsLocationList);
    }

    @Test
    public void givenColourAndCompanyNameOfTheCar_WhenNotPresentInAnyOfTheParkingLot_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(6, 3, attendantNames);
            parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED", "TOYOTA"));
            parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK" , "TOYOTA"));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "BMW"));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "TATA"));
            parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW", "TATA"));
            parkingService.getParkingDetailsOfCarBasedOnColourAndCompany("Black", "BMW");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.NO_SUCH_CAR_PRESENT, e.type);
        }
    }

    @Test
    public void givenCompanyNameOfTheCar_WhenSearchedInAllParkingLots_ShouldReturnListOfLocationsOfCar()
    {
        ParkingService parkingService = new ParkingService(6, 3, attendantNames);
        Car firstCar = new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR, "WHITE", "BMW");
        Car secondCar = new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.LARGE_CAR, "BLACK", "BMW");
        Car thirdCar = new Car("MH-12-UC-4322", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLACK", "BMW");
        parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED", "TOYOTA"));
        parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK" , "TOYOTA"));
        parkingService.parkCar(firstCar);
        parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "TATA"));
        parkingService.parkCar(secondCar);
        parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW", "TATA"));
        parkingService.parkCar(thirdCar);
        List<String> carsLocationList = parkingService.getLocationOfCarBasedOnCompany("BMW");
        List<String> expectedListOfLocation = Arrays.asList(parkingService.getCarLocation(firstCar),
                parkingService.getCarLocation(thirdCar), parkingService.getCarLocation(secondCar));
        Assert.assertEquals(expectedListOfLocation, carsLocationList);
    }

    @Test
    public void givenCompanyNameOfTheCar_WhenNotPresentInAnyOfTheParkingLot_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(6, 3, attendantNames);
            parkingService.parkCar(new Car("MH-22-RT-2324", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "RED", "TOYOTA"));
            parkingService.parkCar(new Car("MH-22-RT-2327", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "BLACK" , "TOYOTA"));
            parkingService.parkCar(new Car("MH-14-OP-2222", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLUE", "TATA"));
            parkingService.parkCar(new Car("MH-18-OC-9923", DriverType.NORMAL_DRIVER, CarType.SMALL_CAR, "YELLOW", "TATA"));
            parkingService.getLocationOfCarBasedOnCompany("BMW");
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.NO_SUCH_CAR_PRESENT, e.type);
        }
    }

    @Test
    public void givenTimeRangeForParkedCars_WhenSearchedForAllTheCars_ShouldReturnListOfLocationsAndPlateNumberOfCar()
    {
        Car firstCar = new Car("MH-26-YU-8884", DriverType.HANDICAP_DRIVER, CarType.SMALL_CAR, "WHITE", "BMW");
        Car secondCar = new Car("MH-18-OC-9922", DriverType.NORMAL_DRIVER, CarType.LARGE_CAR, "BLACK", "BMW");
        Car thirdCar = new Car("MH-12-UC-4322", DriverType.HANDICAP_DRIVER, CarType.LARGE_CAR, "BLACK", "BMW");
        ParkingService parkingService = new ParkingService(6, 3, attendantNames);
        parkingService.parkCar(firstCar);
        parkingService.parkCar(secondCar);
        parkingService.parkCar(thirdCar);
        List<String> carsLocationList = parkingService.getParkingDetailsOfCarWithInProvidedRange(30);
        List<String> expectedListOfLocation = Arrays.asList("( Parking Lot: 1, Parking Slot: 1," +
                        " Plate Number: MH-26-YU-8884 )",
                "( Parking Lot: 2, Parking Slot: 1, Plate Number: MH-18-OC-9922 )",
                "( Parking Lot: 3, Parking Slot: 1, Plate Number: MH-12-UC-4322 )");
        Assert.assertEquals(expectedListOfLocation, carsLocationList);
    }

    @Test
    public void forGivenTimeRangeForParkedCars_WhenNoCarIsPresentInAnyOfTheParkingLot_ShouldGiveException()
    {
        try
        {
            ParkingService parkingService = new ParkingService(6, 3, attendantNames);
            parkingService.getParkingDetailsOfCarWithInProvidedRange(30);
        }
        catch (ParkingLotException e)
        {
            Assert.assertEquals(ParkingLotException.Type.NO_CAR_PARKED, e.type);
        }
    }
}