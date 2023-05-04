package org.dfpl.db.hash.m18010704;
// package 이름은 org.dfpl.db.hash.m학번 입니다. 
// 지키지 않을 시 반려합니다. 
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<I extends Number> implements Set<Integer> {

	// 너무 어려운 학생은 LinkedList 등으로 작성하셔도 됩니다.
	// 최대 3점을 받을 수 있습니다.
	// 멤버 변수는 hashTable 이외의 것을 사용하지 않습니다. (예: size)
	public final static int HASH_TABLE_SIZE = 3;
	private MyThreeWayBTree[] hashTable;
	// 예: private LinkedList<Integer>[] hashTable;

	public MyHashSet() {
		// 해시테이블 배열 크기는 3로 고정합니다.
		// hash function은 key를 3로 나눈 값이며,
		// 충돌시 3-way B-Tree에 저장됩니다.
		hashTable = new MyThreeWayBTree[3];
		for (int i = 0; i < HASH_TABLE_SIZE; i++)
			hashTable[i] = new MyThreeWayBTree();
	}

	@Override
	public int size() {
		// 예제 코드로 수정해도 됩니다. 
		int size = 0;
		for (MyThreeWayBTree t : hashTable) { // 각 트리의 size의 합계산
			size += t.size();
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		for (MyThreeWayBTree t : hashTable) {
			if (t != null && !t.isEmpty()) // 비어 있지 않은 트리가 하나라도 있으면 false
				return false;
		}
		return true;
	}

	@Override
	public boolean contains(Object o) {
		for (MyThreeWayBTree t : hashTable) {
			if (t != null && t.contains(o)) // t에 o가 존재하면 true
				return true;
		}
		return false;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int i = 0; // hashTable의 인덱스 변수
			private Iterator<Integer> iterator = (hashTable.length == 0) ? null : hashTable[i].iterator(); // hashTable의 길이가 0이면 읽을 트리가 없으므로 null

			@Override
			public boolean hasNext() {
				if (iterator == null) {
					return false;
				}
				// 비어있지 않은 트리가 나올때까지 i++ 후 hashTable[i].iterator()를 iterator에 넣음
				while (!iterator.hasNext() && i < hashTable.length - 1) {
					i++;
					iterator = hashTable[i].iterator();
				}

				return iterator.hasNext();
			}

			@Override
			public Integer next() {
				return iterator.next();
			}
		};
	}

	@Override
	public boolean add(Integer e) {
		hashTable[e % hashTable.length].add(e);
		return true; // add 가 잘 수행되었다는 의미
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Integer))
			return false; // 정수가 아닌 경우 삭제 작업 x

		int valueToRemove = (int) o;
		hashTable[valueToRemove % hashTable.length].remove(valueToRemove);
		return true;
	}

	// 테스트 코드에 없어 구현 x
	// **
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}
	// **
}
