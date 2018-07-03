package ru.eltech.ahocorasick.alg.test;

import org.junit.Assert;
import org.junit.Test;
import ru.eltech.ahocorasick.alg.Algorithm;
import ru.eltech.ahocorasick.alg.AlgorithmHistory;
import ru.eltech.ahocorasick.alg.AlgorithmResult;

import java.util.ArrayList;

import static ru.eltech.ahocorasick.alg.test.AlgorithmTest.nRes;

public class AlgorithmHistoryTest {
    @Test
    public void HistoryTest(){
        Algorithm alg = new Algorithm();
        AlgorithmHistory history = new AlgorithmHistory(100);
        alg.addString("A");
        history.save(alg);
        alg.addString("AA");
        history.save(alg);
        alg.addString("AAAA");
        history.save(alg);
        alg.setText("AAAA");
        history.save(alg);
        Assert.assertEquals(history.size(), 4);

        ArrayList<AlgorithmResult> trueRes = new ArrayList<>();
        trueRes.add(nRes(0,0));
        trueRes.add(nRes(0,1));
        trueRes.add(nRes(0,2));
        trueRes.add(nRes(1,0));
        trueRes.add(nRes(1,1));
        trueRes.add(nRes(2,0));
        trueRes.add(nRes(2,1));
        trueRes.add(nRes(3,0));

        alg.finishAlgorithm();
        Assert.assertEquals(alg.getResults(),trueRes);
        alg = history.undo(alg);
        Assert.assertEquals(alg.getResults(),new ArrayList<>());
        alg = history.redo();
        alg.finishAlgorithm();
        Assert.assertEquals(alg.getResults(),trueRes);
        Assert.assertNull(history.redo());

        alg = history.undo(3, alg);
        alg.setText("AAAA");
        trueRes.remove(2);
        alg.finishAlgorithm();
        Assert.assertEquals(alg.getResults(),trueRes);
    }
}