package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.CarType;
import com.bridgelabz.parkinglot.enums.DriverType;

public class Car
{
    public String carNumber;
    public DriverType driverType;
    public CarType carType;

    public Car(String carNumber, DriverType driverType, CarType carType)
    {
        this.carNumber = carNumber;
        this.driverType = driverType;
        this.carType = carType;
    }

    public Car() { }
}
