/*
Question 4: Check Number Parity
-------------------------------
Write a program in Java to check whether the given number is an even number or not using if-else statement.

Sample Input:
12
Sample Output:
No is Even

Sample Input:
13
Sample Output:
No is odd
*/

import java.util.Scanner;

public class CheckNumberParity {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int num = sc.nextInt();

        if (num % 2 == 0) {
            System.out.println("No is Even");
        } else {
            System.out.println("No is odd");
        }

        sc.close();
    }
}
