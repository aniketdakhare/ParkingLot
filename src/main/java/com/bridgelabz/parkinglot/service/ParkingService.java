package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;

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

    public void parkCar(String carNumber)
    {
        IntStream.range(0, totalParkingLots).filter(parkingLot -> parkingLots.get(parkingLot).isCarPresent(carNumber))
                .forEach(i -> { throw new ParkingLotException(ParkingLotException.Type.DUPLICATE_CAR);});
        ParkingLot lot = getLotToPark();
        lot.park(carNumber);
    }

    private ParkingLot getLotToPark()
    {
        int totalCarsInAllLots = IntStream.range(0, totalParkingLots)
                .map(i -> this.parkingLots.get(i).getCarCount()).sum();
        if (totalCarsInAllLots >= (totalParkingSlots * totalParkingLots))
            throw new ParkingLotException(ParkingLotException.Type.LOTS_ARE_FULL);
        List<ParkingLot> selectLot = new ArrayList<>(this.parkingLots);
        selectLot.sort(Comparator.comparing(ParkingLot::getCarCount));
        return selectLot.get(0);
    }

    public boolean isCarPresent(String carNumber)
    {
        return this.parkingLots.stream().anyMatch(lot -> lot.isCarPresent(carNumber));
    }

    public String getCarLocation(String carNumber)
    {
        ParkingLot parkingLot = this.parkingLots.stream().filter(lot -> lot.isCarPresent(carNumber)).findFirst().get();
        return String.format("Parking Lot: %d  Parking Slot: %d", parkingLots.indexOf(parkingLot) + 1,
                parkingLot.carLocation(carNumber));
    }
}