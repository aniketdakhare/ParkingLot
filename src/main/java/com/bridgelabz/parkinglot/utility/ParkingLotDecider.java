package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
import com.bridgelabz.parkinglot.service.ParkingLot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotDecider
{
    private int totalParkingLots;
    private int totalParkingSlots;

    public ParkingLotDecider(int totalParkingLots, int totalParkingSlots)
    {
        this.totalParkingLots = totalParkingLots;
        this.totalParkingSlots = totalParkingSlots;
    }

    public ParkingLot getLotToPark(Car car, List<ParkingLot> parkingLots)
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
}
