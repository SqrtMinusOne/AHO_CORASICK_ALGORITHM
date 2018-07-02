package ru.eltech.ahocorasick.alg.test;

import org.junit.Assert;
import org.junit.Test;
import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.AlgorithmResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class AlgorithmTest {

    private AlgorithmResult nRes(int index, int patternNumber){
        return new AlgorithmResult(index, patternNumber);
    }

    @Test
    public void algorithmFullTest(){
        HashSet<String> strings = new HashSet<String>(4);
        strings.add("a");
        strings.add("ab");
        strings.add("abc");
        strings.add("abcd");
        ArrayList<AlgorithmResult> res = Algorithm.doAhoCorasick("abcdabcdabcd", strings);
        ArrayList<AlgorithmResult> trueRes = new ArrayList<>(Arrays.asList(
                nRes(0,0),
                nRes(0,1),
                nRes(0,2),
                nRes(0,3),
                nRes(4,0),
                nRes(4,1),
                nRes(4,2),
                nRes(4,3),
                nRes(8,0),
                nRes(8,1),
                nRes(8,2),
                nRes(8,3)
        ));
        Assert.assertEquals(res, trueRes);
    }

    @Test
    public void algorithmDisperseTest(){
        Algorithm alg = new Algorithm();
        alg.addString("A");
        alg.addString("AA");
        alg.setText("AAAA");
        ArrayList<AlgorithmResult> trueRes = new ArrayList<>();
        Assert.assertEquals(alg.getText(), "AAAA");
        Assert.assertEquals(alg.getTextPosition(), 0);

        alg.doStep();
        Assert.assertEquals(alg.getTextPosition(), 1);
        trueRes.add(nRes(0,0));
        Assert.assertEquals(alg.getResults(),trueRes);

        alg.doStep();
        Assert.assertEquals(alg.getTextPosition(), 2);
        trueRes.add(nRes(0, 1));
        trueRes.add(nRes(1, 0));
        Assert.assertEquals(alg.getResults(),trueRes);

        alg.finishAlgorithm();
        trueRes.add(nRes(1,1));
        trueRes.add(nRes(2,0));
        trueRes.add(nRes(2,1));
        trueRes.add(nRes(3,0));
        Assert.assertEquals(alg.getResults(),trueRes);

        Assert.assertTrue(alg.getStatus().isOK());

        alg.restart();
        Assert.assertEquals(alg.getTextPosition(), 0);
        Assert.assertEquals(alg.getText(), "AAAA");
        Assert.assertEquals(alg.getResults(), new ArrayList<AlgorithmResult>());

        alg.reset();
        Assert.assertEquals(alg.getText(), "");


    }
}