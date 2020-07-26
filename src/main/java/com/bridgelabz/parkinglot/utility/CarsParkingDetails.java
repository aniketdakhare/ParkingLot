package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.enums.CarType;
import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.service.ParkingLot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarsParkingDetails
{
    public List<String> getLocationOfCarBasedOnColour(String colour, ArrayList<ParkingLot> parkingLots)
    {
        List<String> listOfLocationsOfCar = new ArrayList<>();
        parkingLots.stream().map(lot -> this.getCarsParkingSlotDetailsBasedOnColour(colour, lot))
                .forEachOrdered(carLocationBasedOnColour -> carLocationBasedOnColour.stream()
                        .map(location -> "Parking Lot: " + (location.getParkingLotNumber() + 1) +
                                "  Parking Slot: " + location.getSlotNumber()).forEach(listOfLocationsOfCar::add));
        return listOfLocationsOfCar;
    }

    public List<String> getParkingDetailsOfCarBasedOnColourAndCompany(ArrayList<ParkingLot> parkingLots, String... value)
    {
        List<String> listOfParkingDetailsOfCar = new ArrayList<>();
        parkingLots.forEach(lot -> {
            List<ParkingSlotDetails> carsParkingDetails = new ArrayList<>
                    (this.getCarsParkingSlotDetailsBasedOnColour(value[0], lot));
            carsParkingDetails.retainAll(this.getCarsParkingSlotDetailsBasedOnCarCompany(value[1], lot));
            carsParkingDetails.stream().map(details -> "( Parking Lot: " + (details.getParkingLotNumber() + 1)
                    + ", Parking Slot: " + details.getSlotNumber() + ", Plate Number: " + details.getCar().carNumber
                    + ", Attendant Name: " + details.getAttendantName() + " )")
                    .forEach(listOfParkingDetailsOfCar::add); });
        return listOfParkingDetailsOfCar;
    }

    public List<String> getLocationOfCarBasedOnCompany(String companyName, ArrayList<ParkingLot> parkingLots)
    {
        List<String> listOfLocationsOfCar = new ArrayList<>();
        parkingLots.stream().map(lot -> this.getCarsParkingSlotDetailsBasedOnCarCompany(companyName, lot))
                .forEachOrdered(carLocationBasedOnCompany -> carLocationBasedOnCompany.stream()
                        .map(location -> "Parking Lot: " + (location.getParkingLotNumber() + 1)
                                + "  Parking Slot: " + location.getSlotNumber()).forEach(listOfLocationsOfCar::add));
        return listOfLocationsOfCar;
    }

    public List<String> getParkingDetailsOfCarWithInProvidedRange(int minutes, ArrayList<ParkingLot> parkingLots)
    {
        List<String> listOfParkingDetailsOfCar = new ArrayList<>();
        parkingLots.stream().map(lot -> lot.getParkingMap().values().stream()
                .filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> Duration.between(parkingSlot.getParkedTime(),
                        LocalDateTime.now()).toMinutes() <= minutes)
                .collect(Collectors.toList()))
                .forEachOrdered(parkingDetails -> parkingDetails.stream()
                        .map(location -> "( Parking Lot: " + (location.getParkingLotNumber() + 1) +
                                ", Parking Slot: " + location.getSlotNumber() +
                                ", Plate Number: " + location.getCar().carNumber + " )")
                        .forEach(listOfParkingDetailsOfCar::add));
        return listOfParkingDetailsOfCar;
    }

    public List<String> getLocationAndDetailsOfCarForGivenParkingLot(int parkingLotNumber,
                                                                     ArrayList<ParkingLot> parkingLots)
    {
        return parkingLots.get(parkingLotNumber - 1).getParkingMap().values().stream()
                .filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> parkingSlot.getCar().carType.equals(CarType.SMALL_CAR))
                .filter(parkingSlot -> parkingSlot.getCar().driverType.equals(DriverType.HANDICAP_DRIVER))
                .collect(Collectors.toList()).stream()
                .map(details -> "( Parking Lot: " + (details.getParkingLotNumber() + 1) +
                        ", Parking Slot: " + details.getSlotNumber() +
                        ", Plate Number: " + details.getCar().carNumber +
                        ", Car Company: " + details.getCar().carCompany +
                        ", Car Colour: " + details.getCar().carColour + " )").collect(Collectors.toList());
    }

    private List<ParkingSlotDetails> getCarsParkingSlotDetailsBasedOnColour(String colour, ParkingLot parkingLot)
    {
        return parkingLot.getParkingMap().values().stream().filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> parkingSlot.getCar().carColour.equalsIgnoreCase(colour))
                .collect(Collectors.toList());
    }

    private List<ParkingSlotDetails> getCarsParkingSlotDetailsBasedOnCarCompany(String companyName, ParkingLot parkingLot)
    {
        return parkingLot.getParkingMap().values().stream().filter(parkingSlot -> parkingSlot.getCar() != null)
                .filter(parkingSlot -> parkingSlot.getCar().carCompany.equalsIgnoreCase(companyName))
                .collect(Collectors.toList());
    }

    public List<String> getPlateNumberOfAllParkedCars(ArrayList<ParkingLot> parkingLots)
    {
        List<String> listOfPlateNumberOfCar = new ArrayList<>();
        parkingLots.stream().map(lot -> lot.getParkingMap().values().stream()
                .filter(parkingSlot -> parkingSlot.getCar() != null)
                .collect(Collectors.toList()))
                .forEachOrdered(parkingDetails -> parkingDetails.stream()
                        .map(location -> "Plate Number: " + location.getCar().carNumber)
                        .forEach(listOfPlateNumberOfCar::add));
        return listOfPlateNumberOfCar;
    }
}