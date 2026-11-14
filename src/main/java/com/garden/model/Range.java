package com.garden.model;

public record Range(int min, int max){

    public Range {
        if (min > max) {
            throw new IllegalArgumentException("Range min must not exceed max");
        }
    }

    public boolean contains(int v) {
        return v >= min && v <= max;
    }

    public boolean isBelowRange(int v) {
        return v < min;
    }

    public boolean isAboveRange(int v) {
        return v > max;
    }

    public int midpoint() {
        return (min + max) / 2;
    }

    public int width() {
        return max - min;
    }

    public boolean encloses(Range o) {
        return min <= o.min && max >= o.max;
    }

}