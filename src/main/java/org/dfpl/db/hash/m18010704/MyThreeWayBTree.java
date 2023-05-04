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
		root = null;
	}

	@Override
	public Comparator<? super Integer> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		if (root == null)
			return 0;

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

		while (temp != null) {
			int i = 0;
			List<Integer> keyList = temp.getKeyList();

			while (i < keyList.size() && e > keyList.get(i)) // 키값과 e를 비교
				i++;

			if (i < keyList.size() && e == keyList.get(i)) // 키를 찾은 경우
				return true;
			else { // 키를 찾지 못한 경우
				if (temp.isLeaf()) // 리프 노드까지 이동했는데도 키를 찾지 못한 경우
					return false;

				temp = temp.getChildren().get(i);
			}
		}

		return false;
	}

	private MyThreeWayBTreeNode searchNodeT(Integer e) {
		if (isEmpty())
			return root;

		MyThreeWayBTreeNode temp = root;

		while (temp != null) {
			int i = 0;
			List<Integer> keyList = temp.getKeyList();

			while (i < keyList.size() && e > keyList.get(i)) // 키값과 e를 비교
				i++;

			if (i < keyList.size() && e.equals(keyList.get(i))) // 키를 찾은 경우
				return null; // 중복키 이므로 저장하지 않을것 null 리턴
			else { // 키를 찾지 못한 경우
				if (temp.isLeaf()) // 리프 노드 도착
					return temp;

				temp = temp.getChildren().get(i); // e를 저장할 자식으로 이동
			}
		}

		return null;
	}

	@Override
	public boolean add(Integer e) {
		// 1. Search a leaf node T, that k to be inserted
		MyThreeWayBTreeNode t = searchNodeT(e);
		if (root == null) {
			root = new MyThreeWayBTreeNode(null);
			t = root;
		}
		else if (t == null) // 중복키의 경우
			return false; // 저장 안하고 false리턴

		// 2. if |T| <= m - 1, |T| = the number of keys in T
		while (true) {
			int i = 0;
			List<Integer> keyList = t.getKeyList(); // t의 keyList
			while (i < keyList.size() && e > keyList.get(i)) // 키값과 e를 비교
				i++;
			keyList.add(i, e); // 순서에 맞는 위치에 삽입
			if (keyList.size() <= MAX_KEY_NUM)
				return true;
			else { // 3. else
				// the leaf node splits into three parts (Left, Middle, Right)
				int midIdx = keyList.size() / 2;
				int midKey = keyList.get(midIdx);

				MyThreeWayBTreeNode left = new MyThreeWayBTreeNode(null);
				for (i = 0; i < midIdx; i++)
					left.addKey(keyList.get(i));
				if (!t.isLeaf())
					for (i = 0; i < midIdx + 1; i++)
						left.addChild(t.getChildren().get(i));
//				left.setChildren(t.getChildren().subList(0, midIdx + 1));
				MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(null);
				for (i = midIdx + 1; i < keyList.size(); i++)
					right.addKey(keyList.get(i));
				if (!t.isLeaf())
					for (i = midIdx; i < keyList.size(); i++)
						right.addChild(t.getChildren().get(i));
//				right.setChildren(t.getChildren().subList(midIdx + 1, t.getChildren().size()));
				// middle goes to parent node
				MyThreeWayBTreeNode parent = t.getParent();
				if (parent == null) {
					parent = new MyThreeWayBTreeNode(null);
					root = parent;
					t.setParent(parent);
				}
				// left, right become a left child and a right child of middle, respectively
				left.setParent(parent);
				right.setParent(parent);
				// T = a node contains middle
				t = parent;
				e = midKey;
			}
		}
	}

//	@Override
//	public boolean add(Integer e) {
//		if (root == null) {
//			// 루트 노드가 없으면 새로운 루트 노드를 생성하고 키를 추가합니다.
//			root = new MyThreeWayBTreeNode(null);
//			root.addKey(e);
//			return true;
//		}
//
//		MyThreeWayBTreeNode node = root;
//		while (true) {
//			List<Integer> keyList = node.getKeyList();
//
//			int i = 0;
//			while (i < keyList.size() && e > keyList.get(i))
//				i++;
//
//			if (i < keyList.size() && e.equals(keyList.get(i))) // 이미 존재하는 키인 경우
//				return false;
//
//			if (node.isLeaf()) { // 리프 노드인 경우
//				node.addKey(i, e);
//				if (keyList.size() > MAX_KEY_NUM) // 크기가 MAX_KEY_NUM을 넘어가면
//					split(node); // 분할
//
//				return true;
//			}
//			else { // 내부 노드인 경우
//				MyThreeWayBTreeNode child = node.getChildren().get(i);
//				if (child.getKeyList().size() > MAX_KEY_NUM) // 크기가 MAX_KEY_NUM을 넘어가면
//					split(child); // 자식 노드 분할
//
//				node = child;
//			}
//		}
//	}

//	private void split(MyThreeWayBTreeNode node) {
//		MyThreeWayBTreeNode parent = node.getParent();
//		if (parent == null) {
//			// 루트 노드인 경우
//			parent = new MyThreeWayBTreeNode(null);
//			root = parent;
//			node.setParent(parent);
//			parent.addChild(node);
//		}
//
//		List<Integer> keyList = node.getKeyList();
//		int midIdx = keyList.size() / 2;
//		int midKey = keyList.get(midIdx);
//
//		// 새로운 노드 생성
//		MyThreeWayBTreeNode newNode = new MyThreeWayBTreeNode(parent);
//		for (int i = midIdx + 1; i < keyList.size(); i++) {
//			newNode.addKey(keyList.get(i));
//		}
//
//		List<MyThreeWayBTreeNode> children = node.getChildren();
//		for (int i = midIdx + 1; i < children.size(); i++) {
//			MyThreeWayBTreeNode child = children.get(i);
//			newNode.addChild(child);
//			child.setParent(newNode);
//		}
//
//		// 기존 노드 조정
//		if (midIdx < keyList.size() - 1)
//			keyList.subList(midIdx + 1, keyList.size()).clear();
//		if (midIdx + 1 < children.size())
//			children.subList(midIdx + 1, children.size()).clear();
//
//		// 중간 값 상위 노드로 올리기
//		parent.addKey(midKey);
//		parent.addChild(newNode);
//		newNode.setParent(parent);
//
//		if (parent.getKeyList().size() > MAX_KEY_NUM) // 크기가 MAX_KEY_NUM을 넘어가면
//			split(parent); // 분할
//	}

	private void split(MyThreeWayBTreeNode node) {
		MyThreeWayBTreeNode parent = node.getParent();
		if (parent == null) {
			// 루트 노드인 경우
			parent = new MyThreeWayBTreeNode(null);
			root = parent;
			node.setParent(parent);
			parent.addChild(node);
		}

		List<Integer> keyList = node.getKeyList();
		int midIdx = keyList.size() / 2;
		int midKey = keyList.get(midIdx);

		// 새로운 노드 생성
		MyThreeWayBTreeNode newNode = new MyThreeWayBTreeNode(parent);
		for (int i = midIdx + 1; i < keyList.size(); i++) {
			newNode.addKey(keyList.get(i));
		}

		List<MyThreeWayBTreeNode> children = node.getChildren();
		for (int i = midIdx + 1; i < children.size(); i++) {
			MyThreeWayBTreeNode child = children.get(i);
			newNode.addChild(child);
			child.setParent(newNode);
		}

		// 기존 노드 조정
		if (midIdx < keyList.size())
			keyList.subList(midIdx, keyList.size()).clear();
		if (midIdx + 1 < keyList.size())
			children.subList(midIdx + 1, children.size()).clear();

		// 중간 값 상위 노드로 올리기
		parent.addKey(midKey);
		parent.addChild(newNode);
		newNode.setParent(parent);


		if (parent.getKeyList().size() > MAX_KEY_NUM) // 크기가 MAX_KEY_NUM을 넘어가면
			split(parent); // 분할
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return null;
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
