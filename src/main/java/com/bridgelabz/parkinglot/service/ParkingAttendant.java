package com.bridgelabz.parkinglot.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingAttendant
{
    private int totalParkingLots;
    private int totalParkingSlots;
    public ArrayList<ParkingLot> parkingLots;

    public ParkingAttendant(int slots, int lots)
    {
        this.totalParkingSlots = slots;
        this.totalParkingLots = lots;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0, totalParkingLots)
                .forEach(lotNumber -> parkingLots.add(new ParkingLot(totalParkingSlots)));
    }

    public void parkCar(String carNumber)
    {
        ParkingLot lot = getLotToPark();
        lot.park(carNumber);
    }

    private ParkingLot getLotToPark()
    {
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