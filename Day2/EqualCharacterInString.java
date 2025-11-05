/*
Question 4: Equal Character in String
-------------------------------------
You are given a string s. In one move you can change any character to another character.

You have to make a string which consists of the same character. Find the minimum move to do this task.

Constraints:
1 <= s.length <= 1000
s consists only of lowercase characters.

Input:
aabbbcccc

Output:
5
*/

import java.util.Scanner;

public class EqualCharacterInString {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String s = sc.nextLine();
        int[] frequency = new int[26];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = c - 'a';
            frequency[index]++;
        }

        int maxFrequency = 0;
        for (int i = 0; i < 26; i++) {
            if (frequency[i] > maxFrequency) {
                maxFrequency = frequency[i];
            }
        }

        int minMoves = s.length() - maxFrequency;
        System.out.println(minMoves);

        sc.close();
    }
}
