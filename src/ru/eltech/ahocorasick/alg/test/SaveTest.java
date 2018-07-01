package ru.eltech.ahocorasick.alg.test;

import org.junit.Assert;
import org.junit.Test;
import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.Bohr;
import ru.eltech.ahocorasick.alg.Node;

public class SaveTest {
    @Test
    public void NodeSaveTest(){
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(4);
        Assert.assertFalse(node2.isTemp());
        node1.addSon(node2, 'a');
        node2.setSuffLink(node3);
        node2.setUp(node1);
        node2.addLeaf(10);
        node2.addSon(node1, 'b');
        node2.addSon(node3, 'c');
        String str = node2.toString();
        Node node4 = Node.fromString(str);
        Assert.assertEquals(node4.toString(), str);
        Node node5 = Node.fromString(node1.toString());
        Assert.assertEquals(node5.toString(), node1.toString());
    }

    @Test
    public void BohrSaveTest(){
        Bohr bohr = BohrTest.makeBorExample();
        String str = bohr.toString();
        Bohr bohr2 = Bohr.fromString(str);
        Assert.assertEquals(bohr2.toString(), str);
        BohrTest.checkBohrExample(bohr);
    }

    @Test
    public void AlgorithmSaveTest(){
        Algorithm alg = new Algorithm(new Bohr());
        alg.setText("abcdabcd");
        alg.addString("a");
        alg.addString("abc");
        alg.doStep();
        alg.doStep();
        String str = alg.toString();
        Algorithm alg2 = Algorithm.fromString(str);
        Assert.assertEquals(alg2.toString(), str);
        alg.restart();
        alg2.restart();
        alg.finishAlgorithm();
        alg2.finishAlgorithm();
        Assert.assertEquals(alg.getResults(), alg2.getResults());
    }
}
