/*
Question 4: HashMap
-------------------
Write a Java program to remove an element from a map using the remove() method.
*/

import java.util.*;

public class RemoveElementFromMap {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        String s1 = s.next();
        String s2 = s.next();
        String s3 = s.next();
        String s4 = s.next();
        int remo = s.nextInt();

        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, s1);
        map.put(2, s2);
        map.put(3, s3);
        map.put(4, s4);

        System.out.println("Mappings of HashMap are : " + map);
        map.remove(remo);
        System.out.println("Mappings after removal are : " + map);

        s.close();
    }
}
