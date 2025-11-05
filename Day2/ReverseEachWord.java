/*
Question 3: Reverse Each Word in a String
-----------------------------------------
Write a function to reverse each word in a string.

Example:
Input: great learning
Output: taerg gninrael

Input: hello guys how are you
Output: olleh syug woh era uoy
*/

import java.util.Scanner;

public class ReverseEachWord {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        
        String[] words = input.split(" ");
        StringBuilder finalString = new StringBuilder();

        for (String word : words) {
            finalString.append(new StringBuilder(word).reverse()).append(" ");
        }

        System.out.println(finalString.toString().trim());
        sc.close();
    }
}
