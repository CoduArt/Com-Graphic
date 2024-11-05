package com.cgvsu;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;

public class VertexNormalsCalculator {
    private static final double SCALE = Math.pow(10, 3);

    public static ArrayList<Vector3f> calculateNormals(Model model) {
        ArrayList<Polygon> polygons = model.polygons;
        ArrayList<Vector3f> vertices = model.vertices;
        ArrayList<Vector3f> normals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            normals.add(new Vector3f(0, 0, 0));
        }


        for (Polygon polygon: polygons) {
            ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
            double[] vertex1 = vertices.get(vertexIndices.get(0)).getVector();
            double[] vertex2 = vertices.get(vertexIndices.get(1)).getVector();
            double[] vertex3 = vertices.get(vertexIndices.get(2)).getVector();
            Vector3f vector1 = new Vector3f(vertex2[0] - vertex1[0], vertex2[1] - vertex1[1], vertex2[2] - vertex1[2]);
            Vector3f vector2 = new Vector3f(vertex3[0] - vertex1[0], vertex3[1] - vertex1[1], vertex3[2] - vertex1[2]);

//            double cos = dotVectors(vector1, vector2) / getVectorLength(vector1) * getVectorLength(vector2);
//            double sin = Math.sqrt(1 - Math.pow(cos, 2));

            Vector3f polygonsNormal = crossVectors(vector1, vector2);
//            if (roundUpToPrecision(getVectorLength(polygonsNormal)) != roundUpToPrecision(getVectorLength(vector1) * getVectorLength(vector2) * sin)) {
//                polygonsNormal = divideVector(polygonsNormal, -1);
//            }

            for (Integer i: vertexIndices) {
                normals.set(i, addVector(normals.get(i), polygonsNormal));
            }

            ArrayList<Integer> normalsIndices = new ArrayList<>();
            for (Integer i: vertexIndices) {
                normalsIndices.add(i);
            }
            polygon.setNormalIndices(normalsIndices);
        }

        for (int i = 0; i < normals.size(); i++) {
            normals.set(i, normalizeVector(normals.get(i)));
        }
        model.normals = normals;
        return normals;
    }

    public static Vector3f addVector(Vector3f vector1, Vector3f vector2) {
        double[] v1 = vector1.getVector();
        double[] v2 = vector2.getVector();
        return new Vector3f(v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2]);
    }

    public static double getVectorLength(Vector3f vector) {
        double[] v = vector.getVector();
        double sum = 0;
        for (double coordinate : v) {
            sum += Math.pow(coordinate, 2);
        }
        return Math.sqrt(sum);
    }

    public static Vector3f divideVector(Vector3f vector1, double div) {
        if (div == 0) {
            throw new RuntimeException("division by zero");
        }
        double[] v = vector1.getVector();
        return new Vector3f(v[0] / div, v[1] / div, v[2] / div);
    }

    public static Vector3f crossVectors(Vector3f vector1, Vector3f vector2) {
        double[] v1 = vector1.getVector();
        double[] v2 = vector2.getVector();
        return new Vector3f(
                v1[1] * v2[2] - v1[2] * v2[1],
                - v1[0] * v2[2] + v1[2] * v2[0],
                v1[0] * v2[1] - v1[1] * v2[0]
        );
    }

    public static double dotVectors(Vector3f vector1, Vector3f vector2) {
        double[] v1 = vector1.getVector();
        double[] v2 = vector2.getVector();
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    public static Vector3f normalizeVector(Vector3f vector) {
        double length = getVectorLength(vector);
        return divideVector(vector, length);
    }

    public static double roundUpToPrecision(double val) {
        return Math.ceil(val * SCALE) / SCALE;
    }
}
