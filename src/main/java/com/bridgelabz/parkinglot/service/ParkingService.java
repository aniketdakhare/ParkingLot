package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
import com.bridgelabz.parkinglot.utility.ParkingSlotDetails;
import com.bridgelabz.parkinglot.utility.ParkingLotDecider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingService
{
    private ArrayList<ParkingLot> parkingLots;
    private ParkingLotDecider parkingLotDecider;

    public ParkingService(int slots, int lots, String... attendantNames)
    {
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

    public List<String> getLocationOfCarBasedOnColour(String colour)
    {
        List<String> listOfLocationsOfCar = new ArrayList<>();
        this.parkingLots.stream().map(lot -> lot.getCarsParkingDetailsBasedOnColour(colour))
                .forEachOrdered(carLocationBasedOnColour -> carLocationBasedOnColour.stream()
                        .map(location -> "Parking Lot: " + (location.getParkingLotNumber() + 1) +
                                "  Parking Slot: " + location.getSlotNumber()).forEach(listOfLocationsOfCar::add));
        if (listOfLocationsOfCar.isEmpty())
            throw new ParkingLotException(ParkingLotException.Type.NO_SUCH_CAR_PRESENT);
        return listOfLocationsOfCar;
    }

    public List<String> getParkingDetailsOfCarBasedOnColourAndCompany(String colour, String companyName)
    {
        List<String> listOfParkingDetailsOfCar = new ArrayList<>();
        this.parkingLots.forEach(lot -> {
            List<ParkingSlotDetails> carsParkingDetails = new ArrayList<>(lot.getCarsParkingDetailsBasedOnColour(colour));
            carsParkingDetails.retainAll(lot.getCarsParkingDetailsBasedOnCarCompany(companyName));
            carsParkingDetails.stream().map(details -> "( Parking Lot: " + (details.getParkingLotNumber() + 1)
                    + ", Parking Slot: " + details.getSlotNumber() + ", Plate Number: " + details.getCar().carNumber
                    + ", Attendant Name: " + details.getAttendantName() + " )").forEach(listOfParkingDetailsOfCar::add); });
        if (listOfParkingDetailsOfCar.isEmpty())
            throw new ParkingLotException(ParkingLotException.Type.NO_SUCH_CAR_PRESENT);
        return listOfParkingDetailsOfCar;
    }

    public List<String> getLocationOfCarBasedOnCompany(String companyName)
    {
        List<String> listOfLocationsOfCar = new ArrayList<>();
        this.parkingLots.stream().map(lot -> lot.getCarsParkingDetailsBasedOnCarCompany(companyName))
                .forEachOrdered(carLocationBasedOnCompany -> carLocationBasedOnCompany.stream()
                        .map(location -> "Parking Lot: " + (location.getParkingLotNumber() + 1)
                                + "  Parking Slot: " + location.getSlotNumber()).forEach(listOfLocationsOfCar::add));
        if (listOfLocationsOfCar.isEmpty())
            throw new ParkingLotException(ParkingLotException.Type.NO_SUCH_CAR_PRESENT);
        return listOfLocationsOfCar;
    }

    public List<String> getParkingDetailsOfCarWithInProvidedRange(int minutes)
    {
        List<String> listOfParkingDetailsOfCar = new ArrayList<>();
        this.parkingLots.stream().map(lot -> lot.getParkingDetailsOfCarParkedWithInProvidedTime(minutes))
                .forEachOrdered(parkingDetails -> parkingDetails.stream()
                        .map(location -> "( Parking Lot: " + (location.getParkingLotNumber() + 1) +
                                ", Parking Slot: " + location.getSlotNumber() +
                                ", Plate Number: " + location.getCar().carNumber + " )")
                        .forEach(listOfParkingDetailsOfCar::add));
        if (listOfParkingDetailsOfCar.isEmpty())
            throw new ParkingLotException(ParkingLotException.Type.NO_CAR_PARKED);
        return listOfParkingDetailsOfCar;
    }
}