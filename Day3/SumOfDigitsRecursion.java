/*
Question 2: Sum of All Numbers Using Recursion
----------------------------------------------
Find the sum of the digits using recursion.

Input:
4246
Output:
16

Explanation:
4+2+4+6 = 16

Input:
-32
Output:
5
*/

import java.util.Scanner;

class SumOfDigitsRecursion {

    int sumDigits(int n) {
        n = Math.abs(n); // handle negative numbers
        if (n == 0)
            return 0;
        return (n % 10) + sumDigits(n / 10);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();

        SumOfDigitsRecursion obj = new SumOfDigitsRecursion();
        int result = obj.sumDigits(num);

        System.out.println(result);

        sc.close();
    }
}
