package ru.eltech.ahocorasick.alg.test;

import org.junit.Assert;
import org.junit.Test;
import ru.eltech.ahocorasick.alg.Node;

public class NodeTest {

    @Test
    public void nodeSonTest(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.addSon(node2, 'a');
        Assert.assertEquals(node2.getCharToParent(), 'a');
        Assert.assertEquals(node2.getParent(), node1);
        Assert.assertTrue(node1.isSon('a'));
        Assert.assertFalse(node1.isSon('b'));
        Assert.assertEquals(node1.getSon('a'), node2);
    }

    @Test
    public void nodeTransitionTest(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.addTransition(node2, 'a');
        Assert.assertTrue(node1.isTransitionBy('a'));
        Assert.assertEquals(node1.getTransitionBy('a'), node2);
    }

    @Test
    public void nodeLazyLinksTest(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.setSuffLink(node2);
        node1.setUp(node2);
        Assert.assertEquals(node1.getSuffLink(), node2);
        Assert.assertEquals(node1.getUp(), node2);
    }

    @Test
    public void nodeLeafTest(){
        Node node1 = new Node();
        node1.addLeaf(1);
        Assert.assertTrue(node1.isLeaf());
        Assert.assertTrue(node1.getLeafPatternNumber().contains(1));
    }
}