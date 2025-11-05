/*
Question 5: Target Sum
----------------------
Find indices of two numbers in an array that add up to a given target.
*/

import java.util.*;

class TargetSum {
    static int[] twoSum(int n, int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int arr[] = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = in.nextInt();
        int target = in.nextInt();
        int[] ans = twoSum(n, arr, target);
        System.out.println("(" + ans[0] + " , " + ans[1] + ")");
        in.close();
    }
}
