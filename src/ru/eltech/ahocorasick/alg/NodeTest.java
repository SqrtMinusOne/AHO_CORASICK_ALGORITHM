package ru.eltech.ahocorasick.alg;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void nodeSonTest(){
        Node node1 = new Node();
        Node node2 = new Node();
        node1.addSon(node2, 'a');
        Assert.assertEquals(node2.getCharToParent(), 'a');
        Assert.assertTrue(node2.getParent().equals(node1));
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
}