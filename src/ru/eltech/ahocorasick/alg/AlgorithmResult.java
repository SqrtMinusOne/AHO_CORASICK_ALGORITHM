package ru.eltech.ahocorasick.alg;

import java.util.Comparator;
import java.util.Objects;

public class AlgorithmResult implements Comparable{
    public int getIndex() {
        return index;
    }

    public int getPatternNumber() {
        return patternNumber;
    }

    private final int index;
    private final int patternNumber;

    public AlgorithmResult(int index, int patternNumber) {
        this.index = index;
        this.patternNumber = patternNumber;
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "index=" + index +
                ", patternNumber=" + patternNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlgorithmResult)) return false;
        AlgorithmResult that = (AlgorithmResult) o;
        return index == that.index &&
                patternNumber == that.patternNumber;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        AlgorithmResult that = (AlgorithmResult) o;
        if (index == that.index)
            return patternNumber - that.patternNumber;
        else
            return index - that.index;
    }

    public static class AlgResComparator implements Comparator<AlgorithmResult> {
        @Override
        public int compare(AlgorithmResult o1, AlgorithmResult o2) {
            return o1.compareTo(o2);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, patternNumber);
    }
}

