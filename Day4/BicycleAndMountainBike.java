/*
Question 3: Bicycle and MountainBike
------------------------------------
Implement two classes: Bicycle and MountainBike.

Bicycle:
- Fields: gear, speed
- Methods: applyBrake(), speedUp(), toString()

MountainBike extends Bicycle:
- Field: seatHeight
- Methods: setHeight(), toString()

Demonstrate functionality in main method.
*/

import java.util.Scanner;

public class BicycleAndMountainBike {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        int z = sc.nextInt();

        MountainBike mb = new MountainBike(x, y, z);
        System.out.println(mb.toString());

        sc.close();
    }
}

class Bicycle {
    public int gear;
    public int speed;

    public Bicycle(int gear, int speed) {
        this.gear = gear;
        this.speed = speed;
    }

    public void applyBrake(int decrement) {
        speed -= decrement;
    }

    public void speedUp(int increment) {
        speed += increment;
    }

    public String toString() {
        return ("No of gears are " + gear + "\n" +
                "speed of bicycle is " + speed);
    }
}

class MountainBike extends Bicycle {
    public int seatHeight;

    public MountainBike(int gear, int speed, int startHeight) {
        super(gear, speed);
        seatHeight = startHeight;
    }

    public void setHeight(int newValue) {
        seatHeight = newValue;
    }

    @Override
    public String toString() {
        return (super.toString() + "\nseat height is " + seatHeight);
    }
}
