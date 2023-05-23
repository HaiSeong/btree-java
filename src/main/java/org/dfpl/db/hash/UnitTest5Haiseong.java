package org.dfpl.db.hash;

import org.dfpl.db.hash.m18010704.MyHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class UnitTest5Haiseong {
	public static void main(String[] args) {
		Set<Integer> set1 = new HashSet<Integer>();
		Set<Integer> set2 = new MyHashSet<Integer>();
		// 위의 set2이 아니라 학생들의 MyHashSet으로 동일하게 동작해야 함
		// Set<Integer> set2 = new MyHashSet<Integer>();
		// 과제5는 MyHashSet이 MyThreeWayBTree로 구현되어야만 채점합니다.

		Random r = new Random();
		int [] arr = {3963, 5455, 6024, 5215, 565, 5060, 2158, 799, 4705, 5976, 1835, 6985, 2726, 639, 0, 2284, 4845,
				232, 4089, 1393, 2561, 6719, 2180, 3696, 4651, 2782, 1834, 5120, 4697, 471, 1749, 2459, 3934, 6337,
				1001, 5084, 1829, 3460, 351, 1574, 2663, 5658, 4828, 3162, 3100, 3797, 5505, 4220, 2238, 927, 4484,
				4915, 2633, 4706, 251, 3963, 1590, 3484, 5800, 4729, 5239, 1758, 464, 6178, 6221, 833, 5262, 464,
				5933, 510, 5397, 4970, 4361, 1377, 658, 2151, 6666, 6337, 1472, 6943, 739, 959, 4516, 510,
				761, 384, 3337, 46, 3087, 1792, 5294, 600, 6470, 6589, 3603, 134, 4842, 2405, 1971, 640};
		for (int i = 0; i < 100; i++) {
//			int next = r.nextInt(7000);
			int next = arr[i];
			set1.add(next);
			set2.add(next);
			System.out.print(", " + next);
		}

		// true가 반환되어야만 합니다.
		System.out.println("[1] " + (set1.size() == set2.size()));

		boolean isDifferent = false;
		for (int set1Value : set1) {
			if (!set2.contains(set1Value)) {
				System.out.println(set1Value);
				isDifferent = true;
				break;
			}
		}

		// true가 반환되어야만 합니다.
		System.out.println("[2] " + !isDifferent);

		Iterator<Integer> set2Iter = set2.iterator();
		int cnt = 0;
		while (set2Iter.hasNext()) {
			set2Iter.next();
			cnt++;
		}

		// true가 반환되어야만 합니다.
		System.out.println("[3] " + (set1.size() == cnt));

		ArrayList<Integer> removedList = new ArrayList<Integer>();
		Iterator<Integer> removeIter = set1.iterator();

		int [] arr2 = {0, 5120, 384, 1792, 640, 2561, 5505, 2180, 3460, 4484, 134, 6024, 4361, 3337, 6666, 5262, 3087, 658, 3603, 5397, 5658, 2459, 3100, 3484, 799, 927, 6943, 6178, 4516, 1829, 2726, 1574, 5800, 1834, 1835, 4651, 5933, 46, 5294, 4915, 1971, 565, 1590, 6589, 2238, 6719, 959, 1472, 6337, 833, 5060, 6470, 6985, 2633, 6221, 5455, 464, 1749, 3797, 471, 5976, 600, 4697};
		int rCount = 0;
		while (true) {
			if (rCount == arr2.length)
				break;
//			int valueToRemove = removeIter.next();
			int valueToRemove = arr2[rCount];
			if (valueToRemove == 3797)
				System.out.print(", "+valueToRemove);
			if (valueToRemove == 4697)
				System.out.print(", "+valueToRemove);

//			removeIter.remove();
//			removedList.add(valueToRemove);
			set2.remove(valueToRemove);
			rCount++;

		}

		boolean isNotRemoved = false;
		for (int removed : removedList) {
			if (set2.contains(removed)) {
				isNotRemoved = true;
				break;
			}
		}

		// true가 반환되어야만 합니다.
		System.out.println("[4] " + !isNotRemoved);
	}
}
