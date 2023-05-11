package org.dfpl.db.hash.m18010704;
//package 이름은 org.dfpl.db.hash.m학번 입니다. 

//지키지 않을 시 반려합니다. 

import java.util.*;

import static org.dfpl.db.hash.m18010704.MyThreeWayBTreeNode.MAX_KEY_NUM;

@SuppressWarnings("unused")
public class MyThreeWayBTree implements NavigableSet<Integer> {

	// Data Abstraction은 예시일 뿐 자유롭게 B-Tree의 범주 안에서 어느정도 수정가능
	private MyThreeWayBTreeNode root;

	public MyThreeWayBTree() {
		root = new MyThreeWayBTreeNode(null);
	}

	@Override
	public int size() {
		return root.countTreeSize();
	}

	@Override
	public boolean isEmpty() {
		if (size() == 0)
			return true;

		return false;
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Integer))
			return false;

		int e = (int) o;

		if (isEmpty())
			return false;

		MyThreeWayBTreeNode temp = root;

		while (!temp.isLeaf()) {
			int i = 0;

			while (i < temp.getKeyList().size() && e > temp.getKeyList().get(i)) // 키값과 e를 비교
				i++;

			if (i < temp.getKeyList().size() && e == temp.getKeyList().get(i)) // 키를 찾은 경우
				return true;

			temp = temp.getChildren().get(i);
		}

		return temp.getKeyList().contains(e);

//		Iterator<Integer> iter = iterator();
//		while (iter.hasNext()) {
//			if (iter.next() == e)
//				return true;
//		}
//		return false;
	}

	private MyThreeWayBTreeNode searchNodeT(Integer e) { // 원소를 삽입할 node T를 탐색한다.
		MyThreeWayBTreeNode temp = root;

		while (temp != null) {
			int i = 0;

			while (i < temp.getKeyList().size() && e > temp.getKeyList().get(i)) // 키값과 e를 비교
				i++;

			if (i < temp.getKeyList().size() && e.equals(temp.getKeyList().get(i))) // 키를 찾은 경우
				return null; // 중복키 이므로 저장하지 않을것 null 리턴
			else { // 키를 찾지 못한 경우
				if (temp.isLeaf()) // 리프 노드 도착
				{
					if (temp.getKeyList().contains(e))
						return null;
					return temp;
				}

				temp = temp.getChildren().get(i); // e를 저장할 자식으로 이동
			}
		}

		return null;
	}

	private void InsertKeyIntoLeafNode(MyThreeWayBTreeNode node, Integer e) {
		int i = 0;

		while (i < node.getKeyList().size() && e > node.getKeyList().get(i)) // 키값과 e를 비교
			i++;

		node.getKeyList().add(i, e);
	}

	private void splitLeafNode(MyThreeWayBTreeNode node) {
		int midIdx = node.getKeyList().size() / 2;
		int i;

		if (node.getParent() == null) { // root 노드인 경우
			node.setParent(new MyThreeWayBTreeNode(null));
			root = node.getParent();
			node.getParent().setInternal();
			node.getParent().getKeyList().add(node.getKeyList().get(midIdx));

			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(node.getParent());
			for (i = midIdx + 1; i < node.getKeyList().size(); i++)
				right.getKeyList().add(node.getKeyList().get(i));

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear();

			node.getParent().getChildren().add(node);
			node.getParent().getChildren().add(right);
		}
		else {
			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(node.getParent());
			for (i = midIdx + 1; i < node.getKeyList().size(); i++){
				right.getKeyList().add(node.getKeyList().get(i));
			}

			i = 0;
			while (i < node.getParent().getKeyList().size() && node.getKeyList().get(midIdx) > node.getParent().getKeyList().get(i)) // 키값과 e를 비교
				i++;
			node.getParent().getKeyList().add(i, node.getKeyList().get(midIdx));
			node.getParent().getChildren().add(i + 1, right);

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear();

			if (node.getParent().getKeyList().size() <= MAX_KEY_NUM)
				return;
			else
				splitInternalNode(node.getParent());
		}
	}

	private void splitInternalNode(MyThreeWayBTreeNode node) {
		int midIdx = node.getKeyList().size() / 2;
		int i;


		if (node.getParent() == null) { // root 노드인 경우
			node.setParent(new MyThreeWayBTreeNode(null));
			root = node.getParent();
			node.getParent().getKeyList().add(node.getKeyList().get(midIdx));
			node.getParent().setInternal();

			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(root);
			right.setInternal();
			for (i = midIdx + 1; i < node.getKeyList().size(); i++)
				right.getKeyList().add(node.getKeyList().get(i));

			for (i = midIdx + 1; i < node.getChildren().size(); i++)
				right.getChildren().add(node.getChildren().get(i));

			for (MyThreeWayBTreeNode rightChild : right.getChildren())
				rightChild.setParent(right);

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear();
			node.getChildren().subList(midIdx + 1, node.getChildren().size()).clear();

			node.getParent().getChildren().add(node);
			node.getParent().getChildren().add(right);

		}
		else {
			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(node.getParent());
			right.setInternal();
			for (i = midIdx + 1; i < node.getKeyList().size(); i++)
				right.getKeyList().add(node.getKeyList().get(i));

			for (i = midIdx + 1; i < node.getChildren().size(); i++)
				right.getChildren().add(node.getChildren().get(i));

			for (MyThreeWayBTreeNode rightChild : right.getChildren())
				rightChild.setParent(right);

			i = 0;
			while (i < node.getParent().getKeyList().size() && node.getKeyList().get(midIdx) > node.getParent().getKeyList().get(i)) // 키값과 e를 비교
				i++;
			node.getParent().getKeyList().add(i, node.getKeyList().get(midIdx));
			node.getParent().getChildren().add(i + 1, right);

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear();
			node.getChildren().subList(midIdx + 1, node.getChildren().size()).clear();

			if (node.getParent().getKeyList().size() <= MAX_KEY_NUM)
				return;
			else
				splitInternalNode(node.getParent());
		}
	}

	@Override
	public boolean add(Integer e) {
		// 1. Search a leaf node T, that k to be inserted
		MyThreeWayBTreeNode t = searchNodeT(e);
		if (t == null) // 중복키의 경우
			return false; // 저장 안하고 false리턴
		InsertKeyIntoLeafNode(t, e);

		// 2. if |T| <= m - 1, |T| = the number of keys in T
		int i = 0;
		if (t.getKeyList().size() <= MAX_KEY_NUM)
			return true;
		else { // 3. else
			splitLeafNode(t);
			return true;
		}
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	private class MyThreeWayBTreeIterator implements Iterator<Integer> {
		ArrayList<Integer> arrayList;
		int curIdx;

		public MyThreeWayBTreeIterator() {
			arrayList = root.toArray();
			curIdx = 0;
		}

		@Override
		public boolean hasNext() {
			try {
				int trial = arrayList.get(curIdx);
				return true;
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}

		@Override
		public Integer next() {
			return arrayList.get(curIdx++);
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return new MyThreeWayBTreeIterator();
	}

	@Override
	public Integer lower(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer floor(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer ceiling(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer higher(Integer e) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer pollLast() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NavigableSet<Integer> descendingSet() {
		// TODO Auto-generated method stub
		return null;
	}

	// 테스트 코드에 없어 구현 x
	// **
	@Override
	public Comparator<? super Integer> comparator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer first() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer last() {
		// TODO Auto-generated method stub
		return null;
	}
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
	@Override
	public Iterator<Integer> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NavigableSet<Integer> subSet(Integer fromElement, boolean fromInclusive, Integer toElement,
			boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NavigableSet<Integer> headSet(Integer toElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NavigableSet<Integer> tailSet(Integer fromElement, boolean inclusive) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		// TODO Auto-generated method stub
		return null;
	}
	// **
}
