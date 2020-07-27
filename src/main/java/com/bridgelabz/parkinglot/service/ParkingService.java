package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingService
{
    private int totalParkingLots;
    private int totalParkingSlots;
    public ArrayList<ParkingLot> parkingLots;

    public ParkingService(int slots, int lots)
    {
        this.totalParkingSlots = slots;
        this.totalParkingLots = lots;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0, totalParkingLots)
                .forEach(lotNumber -> parkingLots.add(new ParkingLot(totalParkingSlots)));
    }

    public void parkCar(Car car)
    {
        IntStream.range(0, totalParkingLots).filter(parkingLot -> parkingLots.get(parkingLot).isCarPresent(car))
                .forEach(i -> { throw new ParkingLotException(ParkingLotException.Type.DUPLICATE_CAR);});
        ParkingLot lot = getLotToPark(car);
        lot.park(car);
    }

    public ParkingLot getLotToPark(Car car)
    {
        int totalCarsInAllLots = IntStream.range(0, totalParkingLots)
                .map(lot -> parkingLots.get(lot).getCarCount()).sum();
        if (totalCarsInAllLots >= (totalParkingSlots * totalParkingLots))
            throw new ParkingLotException(ParkingLotException.Type.LOTS_ARE_FULL);
        ParkingLot parkingLot = null;
        List<ParkingLot> selectLot = new ArrayList<>(parkingLots);
        switch (car.carType)
        {
            case SMALL_CAR:
                parkingLot = this.getLotToParkAsPerDriverType(selectLot, car.driverType);
                break;
            case LARGE_CAR:
                selectLot.sort(Comparator.comparing(ParkingLot::getCarCount));
                parkingLot = selectLot.stream()
                        .filter(lot -> lot.getSlotToPark(car.carType) > 0)
                        .findFirst().get();
                break;
        }
        return parkingLot;
    }

    private ParkingLot getLotToParkAsPerDriverType(List<ParkingLot> selectLot, DriverType driverType)
    {
        ParkingLot parkingLot = null;
        switch (driverType)
        {
            case NORMAL_DRIVER:
                selectLot.sort(Comparator.comparing(ParkingLot::getCarCount));
                parkingLot = selectLot.get(0);
                break;
            case HANDICAP_DRIVER:
                int lot = 0;
                if (selectLot.get(lot).getCarCount() == totalParkingSlots)
                    lot++;
                parkingLot = selectLot.get(lot);
                break;
        }
        return parkingLot;
    }

    public void unParkCar(Car car)
    {
        ParkingLot parkingLot = this.getParkingLotOfParkedCar(car);
        parkingLot.unPark(car);
    }

    private ParkingLot getParkingLotOfParkedCar(Car car)
    {
        return this.parkingLots.stream()
                .filter(lot -> lot.isCarPresent(car))
                .findFirst().get();
    }

    public boolean isCarPresent(Car car)
    {
        return this.parkingLots.stream().anyMatch(lot -> lot.isCarPresent(car));
    }

    public String getCarLocation(Car car)
    {
        ParkingLot parkingLot = this.parkingLots.stream()
                .filter(lot -> lot.isCarPresent(car))
                .findFirst().get();
        return String.format("Parking Lot: %d  Parking Slot: %d", parkingLots.indexOf(parkingLot) + 1,
                parkingLot.carLocation(car));
    }
}