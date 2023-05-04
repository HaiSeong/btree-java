package org.dfpl.db.hash;

import static org.junit.Assert.assertTrue;

import org.dfpl.db.hash.m18010704.MyThreeWayBTreeNode;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
        MyThreeWayBTreeNode myThreeWayBTreeNode = new MyThreeWayBTreeNode(null);
        System.out.println(myThreeWayBTreeNode.isLeaf());
        myThreeWayBTreeNode.addKey(1);
        System.out.println(myThreeWayBTreeNode.getKeyList().size());
        MyThreeWayBTreeNode n2 = new MyThreeWayBTreeNode(myThreeWayBTreeNode);


    }
}
