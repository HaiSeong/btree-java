package org.dfpl.db.hash.m18010704;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyThreeWayBTreeTest {
    MyThreeWayBTree tree;
    Random random = new Random();

    @BeforeEach
    public void setUp() {
        tree = new MyThreeWayBTree();
    }

    @Test
    public void testAdd() {
        int[] expectedInput = {3,9,18,12,0,15,6,21,24,27};
        for (int e : expectedInput)
            tree.add(e);
        Iterator iterator = tree.iterator();

        int[] expectedOutput = {0,3,6,9,12,15,18,21,24,27};
        int index = 0;

        while (iterator.hasNext()) {
            int value = (int) iterator.next();
            System.out.println(value);
            assertEquals(expectedOutput[index], value);
            index++;
        }

        assertEquals(expectedOutput.length, index);
    }

    @Test
    public void testContain() {
        ArrayList<Integer> testList = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            int value = random.nextInt(1000) ;
            tree.add(value);
            testList.add(value);
        }

//        Iterator<Integer> iter = tree.iterator();
//        while (iter.hasNext())
//            System.out.print(" " + iter.next());

        for (int i = 0; i < 20000; i++) {
            assertEquals(true, tree.contains(testList.get(i)));
        }
    }
}
