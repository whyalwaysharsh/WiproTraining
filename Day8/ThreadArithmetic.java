/*
Question 1: Thread in Java - Arithmetic Operations
--------------------------------------------------
Build a calculator using multithreading to perform the following arithmetic operations:
Sum, Difference, Product, Ratio, Power.

Input:
6
8

Output:
ARITHEMATIC OPERATIONS
SUM 14
DIFFERENCE -2
PRODUCT 48
RATIO 0
POWER 1679616.0
END OF A
*/

import java.util.*;

public class ThreadArithmetic {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int x = s.nextInt();
        int y = s.nextInt();

        A1 a = new A1(x, y);
        a.start(); // start the thread

        s.close();
    }
}

class A1 extends Thread {
    int i, j;

    A1(int x, int y) {
        i = x;
        j = y;
    }

    public void run() {
        System.out.println("ARITHEMATIC OPERATIONS");
        System.out.println("SUM " + (i + j));
        System.out.println("DIFFERENCE " + (i - j));
        System.out.println("PRODUCT " + (i * j));
        System.out.println("RATIO " + (i / j));
        System.out.println("POWER " + Math.pow(i, j));
        System.out.println("END OF A");
    }
}
