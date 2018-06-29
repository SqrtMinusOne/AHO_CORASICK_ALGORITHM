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
        Node node, node2;
        Assert.assertNotNull((node = bohr.getRoot().getSon('a')));
        Assert.assertNotNull((node = node.getSon('b')));
        node2 = node;
        Assert.assertNotNull((node = node.getSon('c')));
        Assert.assertTrue(node.isLeaf());

        Assert.assertFalse(node2.isLeaf());
        bohr.addString("ab");
        Assert.assertTrue(node2.isLeaf());

        bohr.addString("abd");
        Assert.assertTrue(node2.isSon('d'));
        Assert.assertTrue(node2.getSon('d').isLeaf());
    }

    @Test
    public void bohrLinkTest(){
        Bohr bohr = makeBorExample();
        Node node = bohr.getLink(bohr.getRoot(), 'h');
        node = bohr.getLink(node, 'e');
        Assert.assertTrue(node.getLeafPatternNumber().contains(0));

        node = bohr.getLink(node, 'r');
        node = bohr.getLink(node, 's');
        Assert.assertTrue(node.getLeafPatternNumber().contains(3));

        node = bohr.getLink(node, 'h');
        node = bohr.getLink(node, 'e');
        Assert.assertTrue(node.getLeafPatternNumber().contains(1));

        node = bohr.getUp(node);
        Assert.assertTrue(node.getLeafPatternNumber().contains(0));

        node = bohr.getLink(bohr.getRoot(), 'h');
        node = bohr.getLink(node, 'i');
        node = bohr.getLink(node, 's');
        Assert.assertTrue(node.getLeafPatternNumber().contains(2));

    }

    private static Bohr makeBorExample() {
        Bohr bohr = new Bohr();
        bohr.addString("he");
        bohr.addString("she");
        bohr.addString("his");
        bohr.addString("hers");
        return bohr;
    }

    @Test
    public void bohrStateTest(){
        Bohr bohr = makeBorExample();
        bohr.getNextState('h');
        bohr.getNextState('e');
        Assert.assertTrue(bohr.getState().isLeaf());
        bohr.getNextState('r');
        bohr.getNextState('s');
        Assert.assertTrue(bohr.getState().isLeaf());
        bohr.getNextState('h');
        bohr.getNextState('e');
        Assert.assertTrue(bohr.getState().isLeaf());
    }
}