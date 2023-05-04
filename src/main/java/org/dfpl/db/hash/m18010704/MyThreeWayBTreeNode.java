package org.dfpl.db.hash.m18010704;

//package 이름은 org.dfpl.db.hash.m학번 입니다. 
//지키지 않을 시 반려합니다. 
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MyThreeWayBTreeNode {
	public static final int MAX_KEY_NUM = 2;
	public static final int MAX_CHILD_NUM = 3;
	// Data Abstraction은 예시일 뿐 자유롭게 B-Tree의 범주 안에서 어느정도 수정가능
	private MyThreeWayBTreeNode parent;
	private List<Integer> keyList;
	private List<MyThreeWayBTreeNode> children;

	public MyThreeWayBTreeNode(MyThreeWayBTreeNode parent) {
		this.parent = parent;
		keyList = new ArrayList<Integer>(3);
		children = new ArrayList<MyThreeWayBTreeNode>(4);
	}

	public boolean isLeaf() {
		if (children.isEmpty())
			return true;

		return false;
	}

	public int countTreeSize() {
		int size = keyList.size();

		if (isLeaf())
			return size;

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
}
