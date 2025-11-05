/*
Question 2: SuperClass and Subclasses
-------------------------------------
Create a superclass Vehicle that has a method noOfWheels() of return type void and accepts no parameters. 
Create two subclasses Scooter and Car which override noOfWheels().

Vehicle -> "No of wheels undefined"
Scooter -> "No of wheels 2"
Car -> "No of wheels 4"
*/

public class SuperClassExample {
    public static void main(String[] args) {
        Vehicle v = new Vehicle();
        Scooter s = new Scooter();
        Car c = new Car();

        v.noOfWheels();
        s.noOfWheels();
        c.noOfWheels();
    }
}

class Vehicle {
    void noOfWheels() {
        System.out.println("No of wheels undefined");
    }
}

class Scooter extends Vehicle {
    void noOfWheels() {
        System.out.println("No of wheels 2");
    }
}

class Car extends Vehicle {
    void noOfWheels() {
        System.out.println("No of wheels 4");
    }
}
