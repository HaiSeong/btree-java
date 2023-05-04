package org.dfpl.db.hash;

import org.dfpl.db.hash.m18010704.MyThreeWayBTree;
import org.junit.Test;
        import org.junit.Before;

        import static org.junit.Assert.*;

public class MyThreeWayBTreeTest {
    private MyThreeWayBTree tree;

    @Before
    public void setUp() {
        tree = new MyThreeWayBTree();
    }

    @Test
    public void testAdd() {
        assertTrue(tree.add(5));
        assertTrue(tree.add(3));
        assertTrue(tree.add(7));
        assertTrue(tree.add(2));
        assertTrue(tree.add(4));
        assertTrue(tree.add(6));
        assertTrue(tree.add(8));

        assertFalse(tree.add(3)); // 이미 존재하는 키
        assertFalse(tree.add(5)); // 이미 존재하는 키
        assertFalse(tree.add(8)); // 이미 존재하는 키
    }
}
