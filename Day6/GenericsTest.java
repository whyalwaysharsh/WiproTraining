/*
Question 2: Generics
--------------------
Design a generic class Test to work for Number classes.

Input:
1

Output:
the sum is = 50.0
*/

import java.util.Scanner;

public class GenericsTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                Test<Integer> t1 = new Test<Integer>();
                t1.add(30, 20);
                break;
            case 2:
                Test<Double> t2 = new Test<Double>();
                t2.add(10.0, 20.0);
                break;
            case 3:
                Test<Float> t3 = new Test<Float>();
                t3.add(20.0f, 20.0f);
                break;
        }
    }
}

class Test<T extends Number> {
    void add(T a, T b) {
        System.out.println("the sum is = " + (a.doubleValue() + b.doubleValue()));
    }
}
