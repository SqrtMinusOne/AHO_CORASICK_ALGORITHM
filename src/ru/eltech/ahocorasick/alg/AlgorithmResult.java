package ru.eltech.ahocorasick.alg;

import java.util.Objects;

public class AlgorithmResult{
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
    public int hashCode() {
        return Objects.hash(index, patternNumber);
    }
}
