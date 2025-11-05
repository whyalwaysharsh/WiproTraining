/*
Question 1: Sort the Collection
-------------------------------
Write a Java program to sort an ArrayList in minimal lines of code.

Input:
1
4
5
0

Output:
List before sort: [1, 4, 5, 0]
List after sort: [0, 1, 4, 5]
*/

import java.util.*;
public class SortCollection {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    ArrayList<Integer> toks = new ArrayList<>();
    while(sc.hasNextInt()) toks.add(sc.nextInt());
    sc.close();

    ArrayList<Integer> list;
    if(toks.size()>=2 && toks.get(0) == toks.size()-1){
      list = new ArrayList<>(toks.subList(1, toks.size()));
    } else {
      list = new ArrayList<>(toks);
    }

    System.out.println("List before sort: " + list);
    Collections.sort(list);
    System.out.println("List after sort: " + list);
  }
}
