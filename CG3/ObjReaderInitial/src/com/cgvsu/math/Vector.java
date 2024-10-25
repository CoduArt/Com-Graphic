package com.cgvsu.math;

public abstract class Vector {
    protected double[] vector;

    public double[] getVector() {
        return vector;
    }

    public boolean setVector(double[] vector) {
        if (vector.length != this.vector.length) {
            throw new RuntimeException("The specified vector differs in size from the original one");
        }
        this.vector = vector;
        return true;
    }

    public boolean equals(Vector other) {
        if (other.getVector().length != vector.length) {
            return false;
        }
        final float eps = 1e-7f;
        for (int i = 0; i < vector.length; i++) {
            if (Math.abs(vector[i] - other.getVector()[i]) >= eps)
                return false;
        }
        return true;
    }

}
