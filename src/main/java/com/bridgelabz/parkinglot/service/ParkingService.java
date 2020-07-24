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
        ParkingLot lot = getLotToPark(car.driverType);
        lot.park(car);
    }

    private ParkingLot getLotToPark(DriverType driverType)
    {
        int totalCarsInAllLots = IntStream.range(0, totalParkingLots)
                .map(i -> this.parkingLots.get(i).getCarCount()).sum();
        if (totalCarsInAllLots >= (totalParkingSlots * totalParkingLots))
            throw new ParkingLotException(ParkingLotException.Type.LOTS_ARE_FULL);
        ParkingLot parkingLot = null;
        List<ParkingLot> selectLot = new ArrayList<>(this.parkingLots);
        switch (driverType)
        {
            case NORMAL_DRIVER:
                selectLot.sort(Comparator.comparing(ParkingLot::getCarCount));
                parkingLot = selectLot.get(0);
                break;
            case HANDICAP_DRIVER:
                int i = 0;
                if (selectLot.get(i).getCarCount() == totalParkingSlots)
                    i++;
                parkingLot = selectLot.get(i);
                break;
        }
        return parkingLot;
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