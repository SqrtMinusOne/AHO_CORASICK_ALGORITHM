package ru.eltech.ahocorasick.alg.test;

import org.junit.Assert;
import org.junit.Test;
import ru.eltech.ahocorasick.alg.Bohr;
import ru.eltech.ahocorasick.alg.Node;

public class BohrTest {
    @Test
    public void bohrAddNodeTest(){
        Bohr bohr = new Bohr();
        Node node1 = bohr.addNode(bohr.getRoot(), 'a');
        Node node2 = bohr.addNode(node1, 'b');
        Node node3 = bohr.addNode(node2, 'c');
        Assert.assertTrue(bohr.containsNode(bohr.getRoot()));
        Assert.assertTrue(bohr.containsNode(node1));
        Assert.assertTrue(bohr.containsNode(node2));
        Assert.assertTrue(bohr.containsNode(node3));
        Assert.assertEquals(bohr.getRoot().getSon('a'), node1);
        Assert.assertEquals(node1.getSon('b'), node2);
        Assert.assertEquals(node2.getSon('c'), node3);
    }

    @Test
    public void bohrAddStringTest(){
        Bohr bohr = new Bohr();
        bohr.addString("abc");
        Node node;
        Assert.assertNotNull((node = bohr.getRoot().getSon('a')));
        Assert.assertNotNull((node = node.getSon('b')));
        Assert.assertNotNull((node = node.getSon('c')));
        Assert.assertTrue(node.isLeaf());
    }


}