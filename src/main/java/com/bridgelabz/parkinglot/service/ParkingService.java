package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.ConditionType;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
import com.bridgelabz.parkinglot.utility.CarsParkingDetails;
import com.bridgelabz.parkinglot.utility.ParkingLotDecider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingService
{
    private ArrayList<ParkingLot> parkingLots;
    private ParkingLotDecider parkingLotDecider;
    private CarsParkingDetails carsParkingDetails;

    public ParkingService(int slots, int lots, String... attendantNames)
    {
        this.carsParkingDetails = new CarsParkingDetails();
        this.parkingLots = new ArrayList<>();
        parkingLotDecider = new ParkingLotDecider(lots, slots);
        IntStream.range(0, lots)
                .forEach(lotNumber -> parkingLots.add(new ParkingLot(slots, attendantNames[lotNumber])));
    }

    public void parkCar(Car car)
    {
        IntStream.range(0, parkingLots.size()).filter(parkingLot -> parkingLots.get(parkingLot).isCarPresent(car))
                .forEach(i -> { throw new ParkingLotException(ParkingLotException.Type.DUPLICATE_CAR);});
        ParkingLot lot = parkingLotDecider.getLotToPark(car, this.parkingLots);
        lot.park(car, parkingLots.indexOf(lot));
    }

    public void unParkCar(Car car)
    {
        ParkingLot parkingLot = this.getParkingLotOfParkedCar(car);
        parkingLot.unPark(car);
    }

    public boolean isCarPresent(Car car)
    {
        return this.parkingLots.stream().anyMatch(lot -> lot.isCarPresent(car));
    }

    private ParkingLot getParkingLotOfParkedCar(Car car)
    {
        return this.parkingLots.stream()
            .filter(lot -> lot.isCarPresent(car))
            .findFirst().get();
    }

    public String getCarLocation(Car car)
    {
        ParkingLot parkingLot = this.getParkingLotOfParkedCar(car);
        return String.format("Parking Lot: %d  Parking Slot: %d", parkingLots.indexOf(parkingLot) + 1,
                parkingLot.carLocation(car));
    }

    public <T> List<String> getRequiredDetailsOfCar(ConditionType conditionType, String... value)
    {
        List<String> listOfParkingDetailsOfCar = new ArrayList<>();
        switch (conditionType)
        {
            case COLOUR:
                listOfParkingDetailsOfCar = carsParkingDetails
                        .getLocationOfCarBasedOnColour(value[0], this.parkingLots);
                break;
            case COMPANY:
                listOfParkingDetailsOfCar = carsParkingDetails
                        .getLocationOfCarBasedOnCompany(value[0], this.parkingLots);
                break;
            case COLOUR_AND_COMPANY:
                listOfParkingDetailsOfCar = carsParkingDetails
                        .getParkingDetailsOfCarBasedOnColourAndCompany(this.parkingLots, value);
                break;
            case LOT_NUMBER:
                listOfParkingDetailsOfCar = carsParkingDetails
                        .getLocationAndDetailsOfCarForGivenParkingLot(Integer.parseInt(value[0]), this.parkingLots);
                break;
            case TIME:
                listOfParkingDetailsOfCar = carsParkingDetails
                        .getParkingDetailsOfCarWithInProvidedRange(Integer.parseInt(value[0]), this.parkingLots);
                break;
            case ALL_PARKED_CARS:
                listOfParkingDetailsOfCar = carsParkingDetails.getPlateNumberOfAllParkedCars(this.parkingLots);

        }
        if (listOfParkingDetailsOfCar.isEmpty())
            throw new ParkingLotException(ParkingLotException.Type.CAR_NOT_PRESENT);
        return listOfParkingDetailsOfCar;
    }
}