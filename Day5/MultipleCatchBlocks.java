/*
Question 2: Multiple Catch Blocks
---------------------------------
Catch Arithmetic or Number Format exceptions using multiple catch blocks.
Find if a given number is a factor of 99 or not.
*/

import java.util.Scanner;

public class MultipleCatchBlocks {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        try {
            int n = scn.nextInt();
            try {
                if (99 % n == 0) {
                    System.out.println(n + " is a factor of 99");
                } else {
                    System.out.println(n + " is a not a factor of 99");
                }
            } catch (ArithmeticException e) {
                System.out.println("Arithmetic Exception java.lang.ArithmeticException: / by zero");
            }
        } catch (Exception e) {
            System.out.println("Number Format Exception java.lang.NumberFormatException: For input string: "" + scn.next() + """);
        }

        scn.close();
    }
}
