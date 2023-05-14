package org.dfpl.db.hash.m18010704;
//package 이름은 org.dfpl.db.hash.m학번 입니다. 

//지키지 않을 시 반려합니다. 

import java.util.*;

import static org.dfpl.db.hash.m18010704.MyThreeWayBTreeNode.MAX_KEY_NUM;
import static org.dfpl.db.hash.m18010704.MyThreeWayBTreeNode.MIN_KEY_NUM;

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
		return size() == 0;
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
	}

	private MyThreeWayBTreeNode searchNodeForInsert(Integer e) { // 원소를 삽입할 node T를 탐색한다.
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

			if (node.getParent().getKeyList().size() <= MAX_KEY_NUM) {
			}
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

			if (node.getParent().getKeyList().size() <= MAX_KEY_NUM) {
			}
			else
				splitInternalNode(node.getParent());
		}
	}

	@Override
	public boolean add(Integer e) {
		// 1. Search a leaf node T, that k to be inserted
		MyThreeWayBTreeNode t = searchNodeForInsert(e);
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

	private MyThreeWayBTreeNode searchNodeforRemove(Integer k) {
		MyThreeWayBTreeNode temp = root;

		while (temp != null) {
			int i = 0;

			while (i < temp.getKeyList().size() && k > temp.getKeyList().get(i)) // 키값과 e를 비교
				i++;

			if (i < temp.getKeyList().size() && k.equals(temp.getKeyList().get(i))) // 키를 찾은 경우
				return temp;
			else {
				if (temp.isLeaf()) // 리프 노드 도착
				{
					if (temp.getKeyList().contains(k))
						return temp;
					return null;
				}

				temp = temp.getChildren().get(i); // e를 저장할 자식으로 이동
			}
		}

		return null;
	}

	private void reorganize(MyThreeWayBTreeNode t) {
		MyThreeWayBTreeNode p = t.getParent();
		if (p == null && t.getKeyList().size() == 0){
			root = t.getChildren().get(0);
			root.setParent(null);
			return ;
		}
		if (p == null)
			return;
		int idxT = p.getChildren().indexOf(t);
		MyThreeWayBTreeNode ls = (idxT > 0) ? p.getChildren().get(idxT - 1) : null;
		MyThreeWayBTreeNode rs = (idxT < p.getChildren().size() - 1) ? p.getChildren().get(idxT + 1) : null;
		int lv = (ls != null) ? ls.getKeyList().get(ls.getKeyList().size() - 1) : 0;
		int rv = (rs != null) ? rs.getKeyList().get(0) : 0;
		int plv = (ls != null) ? p.getKeyList().get(idxT - 1) : 0;
		int prv = (rs != null) ? p.getKeyList().get(idxT) : 0;

		if (t.getKeyList().size() < MIN_KEY_NUM) {
			if (ls != null && ls.getKeyList().size() > MIN_KEY_NUM){
				t.getKeyList().add(0, plv);
				p.getKeyList().remove((Integer) plv);
				p.getKeyList().add(idxT - 1, lv);
				ls.getKeyList().remove((Integer) lv);
				if (!t.isLeaf()) {
					for (MyThreeWayBTreeNode node : ls.getChildren())
						node.setParent(t);
					t.getChildren().add(0, ls.getChildren().get(ls.getChildren().size() - 1));
					ls.getChildren().remove(ls.getChildren().size() - 1);
				}
			}
			else if (rs != null && rs.getKeyList().size() > MIN_KEY_NUM){
				t.getKeyList().add(prv);
				p.getKeyList().remove((Integer) prv);
				p.getKeyList().add(rv);
				rs.getKeyList().remove((Integer) rv);
				if (!t.isLeaf()) {
					for (MyThreeWayBTreeNode node : rs.getChildren())
						node.setParent(t);
					t.getChildren().add(rs.getChildren().get(0));
					rs.getChildren().remove(0);
				}
			}
			else {
				if (ls != null){
					ls.getKeyList().add(plv);
					p.getKeyList().remove((Integer) plv);
					if (!t.isLeaf()){
						for (MyThreeWayBTreeNode node : t.getChildren())
							node.setParent(ls);
						ls.getChildren().addAll(t.getChildren());
					}
					p.getChildren().remove(t);
				}
				else {
					t.getKeyList().add(prv);
					p.getKeyList().remove((Integer) prv);
					t.getKeyList().addAll(rs.getKeyList());
					if (!t.isLeaf()){
						for (MyThreeWayBTreeNode node : rs.getChildren())
							node.setParent(t);
						t.getChildren().addAll(rs.getChildren());
					}
					p.getChildren().remove(rs);
				}
				reorganize(p);
			}
		}
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Integer))
			return false;

		int k = (int) o;
		// 지울 원소 k가 포한됨 노드를 찾음
		MyThreeWayBTreeNode t = searchNodeforRemove(k);
		if (t == null)
			return false; // k를 포함하는 노드 t를 찾지못해 false 리턴

		if (t.isLeaf()) { // T가 리프노드인 경우
			t.getKeyList().remove((Integer) k);
			reorganize(t);
		}
		else { // T가 내부노드인 경우
			int idxT = t.getKeyList().indexOf(k);
			MyThreeWayBTreeNode lc = t.getChildren().get(idxT);
			while (!lc.isLeaf())
				lc = lc.getChildren().get(lc.getChildren().size() - 1);
			int lv = lc.getKeyList().get(lc.getKeyList().size() - 1);
			MyThreeWayBTreeNode rc = t.getChildren().get(idxT + 1);
			while (!rc.isLeaf())
				rc = rc.getChildren().get(0);
			int rv = rc.getKeyList().get(0);
			if (lc.getKeyList().size() > MIN_KEY_NUM) {
				lc.getKeyList().remove((Integer) lv);
				t.getKeyList().add(idxT, lv);
				t.getKeyList().remove((Integer) k);
			}
			else if (rc.getKeyList().size() > MIN_KEY_NUM) {
				rc.getKeyList().remove((Integer) rv);
				t.getKeyList().add(idxT, rv);
				t.getKeyList().remove((Integer) k);
			}
			else {
				lc.getKeyList().remove((Integer) lv);
				t.getKeyList().add(idxT, lv);
				t.getKeyList().remove((Integer) k);
				reorganize(lc);
			}
		}

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

		@Override
		public void remove(){
			arrayList.remove(curIdx);
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
