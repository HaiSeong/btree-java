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

		while (!temp.isLeaf()) { // 내부노드인경우 반복
			int i = 0;

			while (i < temp.getKeyList().size() && e > temp.getKeyList().get(i)) // 키값과 e를 비교
				i++;

			if (i < temp.getKeyList().size() && e == temp.getKeyList().get(i)) // 키를 찾은 경우
				return true;

			temp = temp.getChildren().get(i); // 자식으로 이동
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

	private void splitInternalNode(MyThreeWayBTreeNode node) {
		int midIdx;
		if(node.getKeyList().size()%2==0)
				midIdx= node.getKeyList().size() / 2-1;
		else
			midIdx=node.getKeyList().size()/2;
		int i;

		if (node.getParent() == null) { // root 노드를 재조정 하는 경우
			node.setParent(new MyThreeWayBTreeNode(null)); // root에 빈노드를 생성하고 내부노드로 설정
			node.getParent().setInternal();
			root = node.getParent();
			node.getParent().getKeyList().add(node.getKeyList().get(midIdx));

			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(node.getParent()); // 새로운 노드 생성
			if (!node.isLeaf())
				right.setInternal();

			for (i = midIdx + 1; i < node.getKeyList().size(); i++) // right로 key 리스트 복사
				right.getKeyList().add(node.getKeyList().get(i));

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear(); // right로 복사한 원소 삭제

			if (!node.isLeaf()) { // 내부노드인 경우 자식 노드들 복사
				for (i = midIdx + 1; i < node.getChildren().size(); i++)
					right.getChildren().add(node.getChildren().get(i));

				for (MyThreeWayBTreeNode rightChild : right.getChildren())
					rightChild.setParent(right);

				node.getChildren().subList(midIdx + 1, node.getChildren().size()).clear(); // right로 복사한 자식 삭제
			}

			node.getParent().getChildren().add(node); // 부모(루트노드)에 추가하기
			node.getParent().getChildren().add(right);

		}
		else {
			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode(node.getParent()); // 새로운 노드 생성
			if (!node.isLeaf())
				right.setInternal();

			for (i = midIdx + 1; i < node.getKeyList().size(); i++) // right로 key 리스트 복사
				right.getKeyList().add(node.getKeyList().get(i));

			if (!node.isLeaf()) { // 내부노드인 경우 자식 노드들 복사
				for (i = midIdx + 1; i < node.getChildren().size(); i++)
					right.getChildren().add(node.getChildren().get(i));

				for (MyThreeWayBTreeNode rightChild : right.getChildren())
					rightChild.setParent(right);
			}

			i = 0;
			while (i < node.getParent().getKeyList().size() && node.getKeyList().get(midIdx) > node.getParent().getKeyList().get(i)) // 키값과 e를 비교 (mid에 추가할 위치 찾기)
				i++;
			node.getParent().getKeyList().add(i, node.getKeyList().get(midIdx)); // mid는 부모로 올리기
			node.getParent().getChildren().add(i + 1, right); // 부모에 새로 만든 노드 추가

			node.getKeyList().subList(midIdx, node.getKeyList().size()).clear(); // right로 복사한 원소 삭제

			if (!node.isLeaf()) // right로 복사한 자식 삭제
				node.getChildren().subList(midIdx + 1, node.getChildren().size()).clear();

			if (node.getParent().getKeyList().size() > MAX_KEY_NUM) // 부모가 조건을 만족하지 못화면 부모를 분할
				splitInternalNode(node.getParent());
		}
	}

	@Override
	public boolean add(Integer e) {
		// 1. Search a leaf node T, that k to be inserted
		MyThreeWayBTreeNode t = searchNodeForInsert(e);
		if (t == null) // 중복키의 경우
			return false; // 저장 안하고 false리턴
		InsertKeyIntoLeafNode(t, e); // 리프노드 t에 e를 삽입

		// 2. if |T| <= m - 1, |T| = the number of keys in T
		int i = 0;
		if (t.getKeyList().size() <= MAX_KEY_NUM) // 조건을 만족하면 종료
			return true;
		else { // 3. else
			splitInternalNode(t); // 아니면 분할
			return true;
		}
	}

	private MyThreeWayBTreeNode searchNodeForRemove(Integer k) {
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
					if (temp.getKeyList().contains(k)) // leaf에 k가 있었다면
						return temp;
					return null;
				}

				temp = temp.getChildren().get(i); // e를 저장할 자식으로 이동
			}
		}

		return null;
	}

	private void reorganize(MyThreeWayBTreeNode t) {
		MyThreeWayBTreeNode p = t.getParent(); // 부모 노드
		if (p == null && t.isLeaf() && t.getKeyList().size() == 0){ // 부모노드가 루트노드이면서 리프노드인 경우 인데 아무것도 없는경우 : 비어있는 트리이므로 리턴
			return;
		}
		else if (p == null && t.getKeyList().size() < MIN_KEY_NUM){ // 부모노드가 루트노드인경우인데 루트노드가 빈 경우 : 루트노드를 재설정 하기
			root = t.getChildren().get(0);
			root.setParent(null);
			return ;
		}
		else if (p == null) // 루트노드를 만난경우 : 리턴
			return;

		int idxT = p.getChildren().indexOf(t);
		MyThreeWayBTreeNode ls = (idxT > 0) ? p.getChildren().get(idxT - 1) : null; // 왼쪽 형제
		MyThreeWayBTreeNode rs = (idxT < p.getChildren().size() - 1) ? p.getChildren().get(idxT + 1) : null; // 오른쪽 형제
		int lv, rv, plv, prv;

		if (t.getKeyList().size() < MIN_KEY_NUM) { // MIN KEY 속성이 맞지 않는 경우에 조정 필요 : 속성을 만족하는 경우 함수 종료
			if (ls != null && ls.getKeyList().size() > MIN_KEY_NUM) { // 왼쪽 형제가 여유있는경우
				lv = ls.getKeyList().get(ls.getKeyList().size() - 1); // lv, plv 찾기
				plv = p.getKeyList().get(idxT - 1) ;

				t.getKeyList().add(0, plv); // lv 를 부모에게 주고 plv 받아오기
				p.getKeyList().remove((Integer) plv);
				p.getKeyList().add(idxT - 1, lv);
				ls.getKeyList().remove((Integer) lv);

				if (!t.isLeaf()) { // 내부 노드의 경우 lv에 딸려있던 자식 받아오기
					ls.getChildren().get(ls.getChildren().size() - 1).setParent(t);
					t.getChildren().add(0, ls.getChildren().get(ls.getChildren().size() - 1));
					ls.getChildren().remove(ls.getChildren().size() - 1);
				}
			}
			else if (rs != null && rs.getKeyList().size() > MIN_KEY_NUM){ // 오른쪽 형제가 여유있는경우
				rv = rs.getKeyList().get(0); // rv, prv 찾기
				prv = p.getKeyList().get(idxT);

				t.getKeyList().add(prv); // rv 를 부모에게 주고 prv 받아오기
				p.getKeyList().remove((Integer) prv);
				p.getKeyList().add(idxT, rv);
				rs.getKeyList().remove((Integer) rv);

				if (!t.isLeaf()) { // 내부 노드의 경우 rv에 딸려있던 자식 받아오기
					rs.getChildren().get(0).setParent(t);
					t.getChildren().add(rs.getChildren().get(0));
					rs.getChildren().remove(0);
				}
			}
			else { // 두 형제 모두 여유가 없는경우 : 합병
				if (ls != null){ // 왼쪽 형제와 합치기 : ls 에 t를 합침
					plv = p.getKeyList().get(idxT - 1) ;

					ls.getKeyList().add(plv); // 부모에게 plv 받아오기
					p.getKeyList().remove((Integer) plv);

					if (!t.isLeaf()){ // 내부노드의 경우 t의 자식들 데려오기
						for (MyThreeWayBTreeNode node : t.getChildren())
							node.setParent(ls);
						ls.getChildren().addAll(t.getChildren()); // t의 키값 받아오기
					}
					p.getChildren().remove(t); // 노드 t 지움
				}
				else { // 오른쪽 형제와 합치기 : t 에 rs를 합침
					prv = p.getKeyList().get(idxT);

					t.getKeyList().add(prv); // 부모에게 prv 받아오기
					p.getKeyList().remove((Integer) prv);
					t.getKeyList().addAll(rs.getKeyList()); // rs의 원소들 t에 붙이기
					if (!t.isLeaf()){ // 내부노드의 경우 rs의 자식들 데려오기
						for (MyThreeWayBTreeNode node : rs.getChildren())
							node.setParent(t);
						t.getChildren().addAll(rs.getChildren());
					}
					p.getChildren().remove(rs); // rs 지움
				}
				reorganize(p); // 합병후 부모 재조정
			}
		}
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Integer))
			return false;

		int k = (int) o;
		// 지울 원소 k가 포한됨 노드를 찾음
		MyThreeWayBTreeNode t = searchNodeForRemove(k);
		if (t == null)
			return false; // k를 포함하는 노드 t를 찾지못해 false 리턴

		if (t.isLeaf()) { // T가 리프노드인 경우
			t.getKeyList().remove((Integer) k); // 리프노드에서 삭제
			reorganize(t); // 필요시 재조정
		}
		else { // T가 내부노드인 경우
			int idxT = t.getKeyList().indexOf(k);
			MyThreeWayBTreeNode lc = t.getChildren().get(idxT); // 선임자가 있는 노드 찾기
			while (!lc.isLeaf())
				lc = lc.getChildren().get(lc.getChildren().size() - 1);
			int lv = lc.getKeyList().get(lc.getKeyList().size() - 1); // 선임자
			MyThreeWayBTreeNode rc = t.getChildren().get(idxT + 1); // 후임자가 있는 노드 찾기
			while (!rc.isLeaf())
				rc = rc.getChildren().get(0);
			int rv = rc.getKeyList().get(0); // 후임자
			if (lc.getKeyList().size() > MIN_KEY_NUM) { // 선임자가 있는 노드에 여유가 있으면 k를 지우고 선임자를 가져옴
				lc.getKeyList().remove((Integer) lv);
				t.getKeyList().add(idxT, lv);
				t.getKeyList().remove((Integer) k);
			}
			else if (rc.getKeyList().size() > MIN_KEY_NUM) { // 후임자가 있는 노드에 여유가 있으면 k를 지우고 후임자를 가져옴
				rc.getKeyList().remove((Integer) rv);
				t.getKeyList().add(idxT, rv);
				t.getKeyList().remove((Integer) k);
			}
			else { // 둘다 여유가 없으면 우선 선임자를 가져오고 균형을 맞춤
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
			arrayList = root.toArray(); // Node의 toArray 사용
			curIdx = 0;
		}

		@Override
		public boolean hasNext() {
			try {
				int trial = arrayList.get(curIdx); // 현제의 인덱스 리턴
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
