package org.dfpl.db.hash.m18010704;

//package 이름은 org.dfpl.db.hash.m학번 입니다. 
//지키지 않을 시 반려합니다. 
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MyThreeWayBTreeNode {
	// 필요한 변수들
	public static final int MAX_KEY_NUM = 2;
	public static final int MAX_CHILD_NUM = 3;
	public static final int MIN_KEY_NUM = 1;
	// Data Abstraction은 예시일 뿐 자유롭게 B-Tree의 범주 안에서 어느정도 수정가능
	private MyThreeWayBTreeNode parent;
	private List<Integer> keyList;
	private List<MyThreeWayBTreeNode> children;
	private boolean isLeaf = true;

	public MyThreeWayBTreeNode(MyThreeWayBTreeNode parent) {
		this.parent = parent;
		keyList = new ArrayList<Integer>(MAX_KEY_NUM + 1); // 삽입, 삭제중 최대 갯수보다 많이 들어가있는 경우가 있으므로 + 1
		children = new ArrayList<MyThreeWayBTreeNode>(MAX_CHILD_NUM + 1);
	}

	// 키의 갯수를 재귀적으로 구하는 함수
	public int countTreeSize() {
		int size = keyList.size(); // 자신의 키 갯수에

		if (isLeaf()) // 재귀 함수 탈출
			return size;

		// 재귀적으로 자식의 크기를 더함
		for (MyThreeWayBTreeNode child : children) {
			if (child != null)
				size += child.countTreeSize();
		}
		return size;
	}

	public MyThreeWayBTreeNode getParent() {
		return parent;
	}

	public void setParent(MyThreeWayBTreeNode parent) {
		this.parent = parent;
	}

	public void addKey(int key) {
		keyList.add(key);
	}

	public void addKey(int idx, int key) {
		keyList.add(idx, key);
	}

	public List<Integer> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<Integer> keyList) {
		this.keyList = keyList;
	}

	public void addChild(MyThreeWayBTreeNode child) {
		children.add(child);
	}

	public List<MyThreeWayBTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<MyThreeWayBTreeNode> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setInternal() {
		isLeaf = false;
	}

	// array로 리턴하는 함수 (재귀를 이용)
	public ArrayList<Integer> toArray() {
		if (isLeaf) { // 리프노드면
			return (ArrayList<Integer>) keyList;
		}

		ArrayList<Integer> arrayList = new ArrayList<>(); // arrayList 생성
		for (int i = 0; i < keyList.size(); i++) { // 순서에 맞게
			arrayList.addAll(children.get(i).toArray());  // 자식에 대해서 재귀
			arrayList.add(keyList.get(i)); // 자식1과 자식2 사이의 키 추가
		}
		arrayList.addAll(children.get(keyList.size()).toArray()); // 재귀

		return arrayList;
	}
}
