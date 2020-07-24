package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.CarType;
import com.bridgelabz.parkinglot.enums.DriverType;

public class Car
{
    public String carNumber;
    public DriverType driverType;
    public CarType carType;
    public String carColour;

    public Car(String carNumber, DriverType driverType, CarType carType)
    {
        this.carNumber = carNumber;
        this.driverType = driverType;
        this.carType = carType;
    }

    public Car() { }

    public Car(String carNumber, DriverType driverType, CarType carType, String carColour)
    {
        this.carNumber = carNumber;
        this.driverType = driverType;
        this.carType = carType;
        this.carColour = carColour;
    }
}
